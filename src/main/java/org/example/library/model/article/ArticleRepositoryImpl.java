package org.example.library.model.article;

import org.example.exception.ItemNotFoundException;
import org.example.library.AbstractModelRepository;

import java.sql.SQLException;
import java.util.Objects;

public class ArticleRepositoryImpl extends AbstractModelRepository<Article> implements ArticleRepository {

    private final static String TABLE_NAME = "articles";
    private static ArticleRepositoryImpl instance;

    private ArticleRepositoryImpl() {
        super(TABLE_NAME);
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

    public static synchronized ArticleRepositoryImpl getInstance() {
        if (Objects.nonNull(instance)) {
            return instance;
        }
        try {
            instance = new ArticleRepositoryImpl();
            instance.createTable();
            return instance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
