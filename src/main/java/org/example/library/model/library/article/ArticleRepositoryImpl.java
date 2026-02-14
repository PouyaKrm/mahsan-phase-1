package org.example.library.model.library.article;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.library.AbstractLibraryRepository;
import org.example.sql.JdbcConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class ArticleRepositoryImpl extends AbstractLibraryRepository<Article> implements ArticleRepository {

    private final static String TABLE_NAME = "articles";
    private static ArticleRepositoryImpl instance;

    private ArticleRepositoryImpl(Connection connection) {
        super(TABLE_NAME, connection);
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
    public Article getOne(Long id) throws SQLException, ItemNotFoundException {
        return super.getOne(id, Article.class);
    }

    public static synchronized ArticleRepositoryImpl getInstance(Connection connection) {
        if (Objects.nonNull(instance)) {
            return instance;
        }
        try {
            instance = new ArticleRepositoryImpl(connection);
            instance.createTable();
            return instance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArticleRepositoryImpl getInstance() {
        return getInstance(JdbcConnection.getConnection());
    }
}
