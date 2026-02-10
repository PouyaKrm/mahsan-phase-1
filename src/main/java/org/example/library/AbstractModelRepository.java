package org.example.library;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.BaseModel;
import org.example.library.model.DBFieldMapping;
import org.example.library.model.ModelFactory;
import org.example.library.model.ModelRepository;
import org.example.sql.JdbcConnection;
import org.example.utils.DateUtils;
import org.example.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class AbstractModelRepository<T extends BaseModel> implements ModelRepository<T> {

    protected final Connection connection = JdbcConnection.getConnection();
    protected final String tableName;
    private final ModelFactory modelFactory = ModelFactory.getInstance();
    private final Map<String, DBFieldMapping> fieldMappings = initializeMappings();
    private final String ID_COLUMN = "id";

    protected AbstractModelRepository(String tableName, Map<String, DBFieldMapping> fieldMappings) {
        this.tableName = tableName;
        addMapping(fieldMappings);
    }

    protected AbstractModelRepository(String tableName) {
        this.tableName = tableName;
    }

    private static Map<String, DBFieldMapping> initializeMappings() {
        Map<String, DBFieldMapping> mappings = new HashMap<>();
        mappings.put("id", new DBFieldMapping("id", "INT NOT NULL AUTO_INCREMENT PRIMARY KEY", (BaseModel model, String id) -> model.setId(Long.parseLong(id)), model -> model.getId(), Types.BIGINT));
        mappings.put("title", new DBFieldMapping("title", "VARCHAR(100) NOT NULL", BaseModel::setTitle, BaseModel::getTitle, Types.VARCHAR));
        mappings.put("author", new DBFieldMapping("author", "VARCHAR(100) NOT NULL", BaseModel::setAuthor, BaseModel::getAuthor, Types.VARCHAR));
        mappings.put("content", new DBFieldMapping("content", "TEXT NOT NULL", BaseModel::setContent, BaseModel::getContent, Types.VARCHAR));
        mappings.put("pubDate", new DBFieldMapping("pub_date", "INT UNSIGNED NOT NULL", (BaseModel model, String value) -> model.setPubDate(parseDate(value)), BaseModel::getPubDateEpochDay, Types.BIGINT));
        mappings.put("borrowDate", new DBFieldMapping("borrow_date", "INT UNSIGNED", (BaseModel model, String value) -> model.setBorrowDate(parseDate(value)), BaseModel::getBorrowDateEpochDay, Types.BIGINT));
        return mappings;
    }

    private static LocalDate parseDate(String date) {
        if (Objects.isNull(date)) {
            return null;
        }
        return DateUtils.createDateEpochDay(Integer.parseInt(date));
    }

    private void addMapping(Map<String, DBFieldMapping> newMappings) {
        for (var mapping : newMappings.entrySet()) {
            if (fieldMappings.containsKey(mapping.getKey())) {
                fieldMappings.replace(mapping.getKey(), mapping.getValue());
            } else {
                fieldMappings.put(mapping.getKey(), mapping.getValue());
            }
        }
    }

    protected void createTable() throws SQLException {
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


    protected T[] getAll(Class<T> tClass) throws SQLException {
        List<T> articleList = new ArrayList<>();
        var factory = modelFactory.getFactory(tClass);
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
        var factory = modelFactory.getFactory(tClass);
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
        return Objects.isNull(model.getId()) ? insertAll(List.of(model)).getFirst() : updateModel(model);
    }

    @Override
    public T[] saveAll(T[] models, Class<T> tClass) throws SQLException {
        var r = insertAll(Arrays.stream(models).toList());
        return Utils.listToArray(r, tClass);
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

    private List<T> insertAll(List<T> models) throws SQLException {
        var builder = new StringBuilder("INSERT INTO " + tableName + " ( ");
        var fields = fieldMappings.values().stream().filter(field -> !field.dbFieldName().equals(ID_COLUMN)).toList();
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
}




