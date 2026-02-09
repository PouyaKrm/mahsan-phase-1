package org.example.library.model.magazine;

import org.example.exception.ItemNotFoundException;
import org.example.library.AbstractModelRepository;
import org.example.library.model.article.Article;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Objects;

public class MagazineRepositoryImpl extends AbstractModelRepository<Magazine> implements MagazineRepository {

    private static MagazineRepositoryImpl instance;
    private static final String TABLE_NAME = "magazines";

    protected MagazineRepositoryImpl() {
        super(TABLE_NAME);
    }


    @Override
    public Magazine[] getAll() throws SQLException {
        return getAll(Magazine.class);
    }

    @Override
    public Magazine[] removeAll() throws SQLException {
        return removeAll(Magazine.class);
    }

    @Override
    public Magazine save(Magazine model) throws SQLException {
        return Objects.isNull(model.getId()) ? insertInto(model) : update(model);
    }

    @Override
    public Magazine getOne(Long id) throws SQLException, ItemNotFoundException {
        return getOne(id, Magazine.class);
    }

    private Magazine insertInto(Magazine model) throws SQLException {
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

    private Magazine update(Magazine model) throws SQLException {
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

    public static synchronized MagazineRepositoryImpl getInstance() {
        if(Objects.nonNull(instance)) {
            return instance;
        }
        try {
            instance = new MagazineRepositoryImpl();
            instance.createTable();
            return instance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
