package org.example.library.model;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.library.ModelAbstractFactory;
import org.example.sql.JdbcConnection;
import org.example.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class AbstractModelRepository<T extends BaseModel> implements ModelRepository<T> {

    protected final Connection connection;
    protected final String tableName;
    private final ModelAbstractFactory modelFactory = ModelAbstractFactory.getInstance();
    private final DBFieldMapping idDBField = new DBFieldMapping<BaseModel>("id", "INT NOT NULL AUTO_INCREMENT PRIMARY KEY", (BaseModel model, String id) -> model.setId(Long.parseLong(id)), BaseModel::getId, Types.BIGINT);
    private final String ID_COLUMN = "id";
    private final Map<String, DBFieldMapping> fieldMappings = new HashMap<>();

    protected AbstractModelRepository(String tableName, Map<String, DBFieldMapping> fieldMappings) {
        this.tableName = tableName;
        this.connection = JdbcConnection.getConnection();
        addMapping(fieldMappings);
    }

    protected AbstractModelRepository(String tableName, Map<String, DBFieldMapping> fieldMappings, Connection connection) {
        this.tableName = tableName;
        this.connection = connection;
        addMapping(fieldMappings);
    }


    protected AbstractModelRepository(String tableName, Connection connection, Map<String, DBFieldMapping> fieldMappings) {
        this.tableName = tableName;
        this.connection = connection;
        addMapping(fieldMappings);
    }


    private void addMapping(Map<String, DBFieldMapping> newMappings) {
        fieldMappings.put(ID_COLUMN, idDBField);
        for (var mapping : newMappings.entrySet()) {
            if (fieldMappings.containsKey(mapping.getKey())) {
                fieldMappings.replace(mapping.getKey(), mapping.getValue());
            } else {
                fieldMappings.put(mapping.getKey(), mapping.getValue());
            }
        }
    }

    private void createTable(String tableName) throws SQLException {
        var createTableStatement = """
                CREATE TABLE IF NOT EXISTS
                """;
        var bs = new StringBuffer(createTableStatement);
        bs.append(" ").append(tableName).append("( ");
        for (var entry : fieldMappings.entrySet()) {
            bs.append(entry.getValue().getDBField()).append(", ");
        }
        bs.append("PRIMARY KEY (id)");
        bs.append(" );");
        var st = connection.createStatement();
        st.execute(bs.toString());
    }

    protected void createTable() throws SQLException {
        createTable(tableName);
    }

    protected T[] getAll(Class<T> tClass) throws SQLException {
        List<T> articleList = new ArrayList<>();
        var factory = modelFactory.getDefaultFactory(tClass);
        PreparedStatement ps = connection.prepareStatement("select * from " + tableName);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            try {
                var model = tClass.getConstructor().newInstance();
                factory.populateFromDB(model, rs, fieldMappings.values());
                articleList.add(model);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return Utils.listToArray(articleList, tClass);
    }

    protected T[] removeAll(Class<T> tClass) throws SQLException {
        var ts = getAll(tClass);
        PreparedStatement ps = connection.prepareStatement("DELETE FROM " + tableName);
        ps.executeUpdate();
        return ts;
    }

    @Override
    public boolean removeOne(Long id) throws SQLException {
        var s = MessageFormat.format("DELETE FROM {0} WHERE id = ?", tableName);
        var pst = connection.prepareStatement(s);
        pst.setLong(1, id);
        return pst.executeUpdate() > 0;
    }

    @Override
    public boolean removeOne(T model) throws SQLException {
        var s = MessageFormat.format("DELETE FROM {0} WHERE id = ?", tableName);
        var pst = connection.prepareStatement(s);
        pst.setLong(1, model.getId());
        return pst.executeUpdate() > 0;
    }

    protected T getOne(Long id, Class<T> tClass) throws SQLException, ItemNotFoundException {
        var s = MessageFormat.format("SELECT * FROM {0} WHERE id = ?", tableName);
        var pst = connection.prepareStatement(s);
        var factory = modelFactory.getDefaultFactory(tClass);
        pst.setLong(1, id);
        var rs = pst.executeQuery();
        if (!rs.next()) {
            throw new ItemNotFoundException(MessageFormat.format("No item found with id {0}", id));

        }
        try {
            var model = tClass.getConstructor().newInstance();
            return factory.populateFromDB(model, rs, fieldMappings.values());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T save(T model) throws SQLException {
        return Objects.isNull(model.getId()) ? insertAll(List.of(model), tableName).getFirst() : updateAll(List.of(model)).getFirst();
    }

    @Override
    public T[] saveAll(T[] models, Class<T> tClass) throws SQLException {
        connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        connection.setAutoCommit(false);
        var updated = Arrays.stream(models).filter(model -> Objects.nonNull(model.getId())).toList();
        var newModels = Arrays.stream(models).filter(model -> Objects.isNull(model.getId())).toList();
        insertAll(newModels, tableName);
        updateAll(updated);
        connection.commit();
        return models;
    }

    private T updateModel(T model) throws SQLException {
        var builder = new StringBuilder("UPDATE" + tableName + " SET ");
        var fields = fieldMappings.values().stream().filter(field -> !field.dbFieldName().equals(ID_COLUMN)).toList();
        fields.forEach(field -> builder.append(field.dbFieldName()).append(" = ? \n"));
        builder.append("WHERE ").append(ID_COLUMN).append(" = ?;");
        var pst = connection.prepareStatement(builder.toString(), Statement.RETURN_GENERATED_KEYS);
        AtomicInteger index = new AtomicInteger(1);
        fields.forEach(field -> setPrepareStatement(index.getAndIncrement(), field, pst, model));
        pst.setLong(fields.size(), model.getId());
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            long generatedId = rs.getLong(1);
            model.setId(generatedId);
        }

        return model;
    }

    private void setPrepareStatement(int index, DBFieldMapping fieldMapping, PreparedStatement pst, T model) {
        try {
            if (Objects.isNull(fieldMapping.toDB().apply(model))) {
                pst.setNull(index, fieldMapping.dbType());
            } else {
                pst.setObject(index, fieldMapping.toDB().apply(model), fieldMapping.dbType());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private List<T> insertAll(List<T> models, String tableName) throws SQLException {
        var builder = new StringBuilder("INSERT INTO " + tableName + " ( ");
        var fields = getNoneIdFields();
        var columns = fields.stream().map(DBFieldMapping::dbFieldName).filter(s -> !s.equals(ID_COLUMN)).collect(Collectors.joining(", "));
        builder.append(columns).append(" ) ");
        builder.append("VALUES ");
        var values = createValuesSql();
        builder.append(values).append(";");
        var pst = connection.prepareStatement(builder.toString(), Statement.RETURN_GENERATED_KEYS);
        models.forEach(model -> addBatche(pst, model, fields));
        pst.executeBatch();
        pst.clearParameters();
        ResultSet rs = pst.getGeneratedKeys();
        var i = 0;
        while (rs.next()) {
            long generatedId = rs.getLong(1);
            models.get(i++).setId(generatedId);
        }
        return models;
    }

    private List<T> updateAll(List<T> models) throws SQLException {
        connection.setAutoCommit(false);
        var random = new Random();
        var temp_table_name = "temp_" + tableName + random.nextLong(0, 10000);
        createTable(temp_table_name);
        insertAll(models, temp_table_name);
        var updateStatement = new StringBuilder("UPDATE ");
        updateStatement.append(tableName).append(" set ");
        var fieldsStatement = getNoneIdFields().stream().map(
                field -> MessageFormat.format("{0} = (select tmp.{1} from {2} tmp where tmp.{3} = {4}.{5}) ", field.dbFieldName(), field.dbFieldName(), temp_table_name, ID_COLUMN, tableName, ID_COLUMN)
        ).collect(Collectors.joining(", "));
        updateStatement.append(fieldsStatement);
        updateStatement.append("where ")
                .append(ID_COLUMN)
                .append(" IN ")
                .append(" (select ")
                .append(ID_COLUMN)
                .append(" FROM ")
                .append(temp_table_name)
                .append(" )");
        updateStatement.append(";");
        var st = connection.createStatement();
        st.execute(updateStatement.toString());
        st.execute("DROP TABLE " + temp_table_name);
        return models;
    }

    private String createValuesSql() {
        var builder = new StringBuilder();
        builder.append(" ( ");
        var values = fieldMappings.values().stream().filter(s -> !s.dbFieldName().equals(ID_COLUMN)).map(field -> "?").collect(Collectors.joining(", "));
        builder.append(values);
        builder.append(" ) ");
        return builder.toString();
    }

    private void addBatche(PreparedStatement pst, T model, List<DBFieldMapping> nonIdFields) {
        AtomicInteger index = new AtomicInteger(1);
        nonIdFields.forEach(field -> setPrepareStatement(index.getAndIncrement(), field, pst, model));
        try {
            pst.addBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DBFieldMapping> getNoneIdFields() {
        return fieldMappings.values().stream().filter(field -> !field.dbFieldName().equals(ID_COLUMN)).toList();
    }

    protected Map<String, DBFieldMapping> getFieldMappings() {
        return fieldMappings;
    }
}




