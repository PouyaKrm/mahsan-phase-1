package org.example.library;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.BaseModel;
import org.example.library.model.DBFieldMapping;
import org.example.library.model.ModelFactory;
import org.example.library.model.ModelRepository;
import org.example.library.model.article.Article;
import org.example.library.model.book.Book;
import org.example.sql.JdbcConnection;
import org.example.utils.Utils;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

public abstract class AbstractModelRepository<T extends BaseModel> implements ModelRepository<T> {

    protected final Connection connection = JdbcConnection.getConnection();
    protected final String tableName;
    private final ModelFactory modelFactory = ModelFactory.getInstance();
    private final Map<String, DBFieldMapping> fieldMappings;

    protected AbstractModelRepository(String tableName, Map<String, DBFieldMapping> fieldMappings) {
        this.tableName = tableName;
        this.fieldMappings = fieldMappings;
    }

    protected void createTable() throws SQLException {
        var createTableStatement = """
                CREATE TABLE IF NOT EXISTS
                """;
        var bs = new StringBuffer(createTableStatement);
        bs.append(" " + tableName + "( ");
        fieldMappings.replace("id", new DBFieldMapping("id", "id", "INT NOT NULL AUTO_INCREMENT"));
        for (var entry : fieldMappings.entrySet()) {
            bs.append(entry.getValue().getDBField() + ", ");
        }
        bs.append("PRIMARY KEY (id)");
        bs.append(" );");
        var st = connection.createStatement();
        st.execute(bs.toString());
    }

    ;


    protected T[] getAll(Class<T> tClass) throws SQLException {
        List<T> articleList = new ArrayList<>();
        var factory = modelFactory.getFactory(tClass);
        PreparedStatement ps = connection.prepareStatement("select * from " + tableName);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            articleList.add(factory.createFromResultSet(rs));
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
        if (rs.next()) {
            return factory.createFromResultSet(rs);
        } else {
            throw new ItemNotFoundException(MessageFormat.format("No item found with id {0}", id));
        }
    }

    protected final Map<String, DBFieldMapping> getFieldMappings() {
        return fieldMappings;
    }
}
