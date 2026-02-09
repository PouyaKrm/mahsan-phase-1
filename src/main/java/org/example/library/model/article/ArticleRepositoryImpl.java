package org.example.library.model.article;

import org.example.exception.ItemNotFoundException;
import org.example.library.AbstractModelRepository;
import org.example.library.model.DBFieldMapping;
import org.example.sql.JdbcConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ArticleRepositoryImpl extends AbstractModelRepository<Article> implements ArticleRepository {

    private static final Connection connection = JdbcConnection.getConnection();
    private final static String TABLE_NAME = "articles";
    private final static String ID_COLUMN = "id";
    private static ArticleRepositoryImpl instance;
    private final ArticleFactory factory = ArticleFactory.getFactory();

    private ArticleRepositoryImpl() {
        super(TABLE_NAME, createFieldMappings());
    }

    @Override
    public Article[] getAll() throws SQLException {
        return super.getAll(Article.class);
    }

    @Override
    public Article[] removeAll() throws SQLException {
        return removeAll(Article.class);
    }

    @Override
    public Article save(Article model) throws SQLException {
        return Objects.isNull(model.getId()) ? insertInto(model) : update(model);
    }


    private Article insertInto(Article model) throws SQLException {
        var st = MessageFormat.format("INSERT INTO {0} (title, author, content, pub_date, borrow_date) VALUES (?,?,?,?,?)", tableName);
        var pst = connection.prepareStatement(st, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, model.getTitle());
        pst.setString(2, model.getAuthor());
        pst.setString(3, model.getContent());
        pst.setLong(4, model.getPubDate().toEpochDay());
        if (Objects.nonNull(model.getBorrowDate())) {
            pst.setLong(5, model.getBorrowDate().toEpochDay());
        } else {
            pst.setNull(5, java.sql.Types.BIGINT);
        }
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            long generatedId = rs.getLong(1);
            model.setId(generatedId);
        }

        return model;
    }

    private Article update(Article model) throws SQLException {
        var st = MessageFormat.format("UPDATE {0} SET title=?, author=?, content=?, pub_date=?, borrow_date=? WHERE id=?", tableName);
        var pst = connection.prepareStatement(st, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, model.getTitle());
        pst.setString(2, model.getAuthor());
        pst.setString(3, model.getContent());
        pst.setLong(4, model.getPubDate().toEpochDay());
        if (Objects.nonNull(model.getBorrowDate())) {
            pst.setLong(5, model.getBorrowDate().toEpochDay());
        } else {
            pst.setNull(5, java.sql.Types.BIGINT);
        }
        pst.setLong(6, model.getId());
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            long generatedId = rs.getLong(1);
            model.setId(generatedId);
        }

        return model;
    }


    @Override
    public Article getOne(Long id) throws SQLException, ItemNotFoundException {
        return super.getOne(id, Article.class);
    }

    public static synchronized ArticleRepositoryImpl getInstance() {
        if (Objects.nonNull(instance)) {
            return instance;
        }
        createFieldMappings();
        try {
            instance = new ArticleRepositoryImpl();
            instance.createTable();
            return instance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, DBFieldMapping> createFieldMappings() {
        Map<String, DBFieldMapping> fieldMappings = new HashMap<>();
        fieldMappings.put("id", new DBFieldMapping("id", "id", "INT NOT NULL AUTO_INCREMENT PRIMARY KEY"));
        fieldMappings.put("title", new DBFieldMapping("title", "title", "VARCHAR(100) NOT NULL"));
        fieldMappings.put("author", new DBFieldMapping("author", "author", "VARCHAR(100) NOT NULL"));
        fieldMappings.put("content", new DBFieldMapping("content", "content", "TEXT NOT NULL"));
        fieldMappings.put("pubDate", new DBFieldMapping("pubDate", "pub_date", "INT UNSIGNED NOT NULL"));
        fieldMappings.put("borrowDate", new DBFieldMapping("borrowDate", "borrow_date", "INT UNSIGNED"));
        return fieldMappings;
    }
}
