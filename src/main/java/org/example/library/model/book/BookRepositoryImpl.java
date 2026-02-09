package org.example.library.model.book;

import org.example.exception.ItemNotFoundException;
import org.example.library.AbstractModelRepository;
import org.example.library.model.BaseModel;
import org.example.library.model.DBFieldMapping;
import org.example.library.model.ModelRepository;
import org.example.sql.JdbcConnection;
import org.example.utils.Utils;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

public class BookRepositoryImpl extends AbstractModelRepository<Book> implements BookRepository {


    private final BookFactory factory = BookFactory.getFactory();
    private static BookRepositoryImpl instance;


    protected BookRepositoryImpl() {
        super("books", createFieldMappings());
    }

    private static Map<String, DBFieldMapping> createFieldMappings() {
        Map<String, DBFieldMapping> fieldMappings = new HashMap<>();
        fieldMappings.put("status", new DBFieldMapping(
                "status",
                "VARCHAR(20) NOT NULL",
                (BaseModel model, String value) -> ((Book) model).setStatus(Book.Status.valueOf(value))));
        return fieldMappings;
    }


    public static BookRepositoryImpl getInstance() {
        if (Objects.nonNull(instance)) {
            return instance;
        }
        synchronized (BookRepositoryImpl.class) {
            try {
                instance = new BookRepositoryImpl();
                instance.createTable();
                return instance;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public Book[] getAll() throws SQLException {
        return super.getAll(Book.class);
    }

    @Override
    public Book[] removeAll() throws SQLException {
        return super.removeAll(Book.class);
    }

    @Override
    public Book save(Book model) throws SQLException {
        return Objects.isNull(model.getId()) ? insertInto(model) : update(model);
    }

    private Book insertInto(Book model) throws SQLException {
        var st = MessageFormat.format("INSERT INTO {0} (title, author, content, pub_date, status, borrow_date) VALUES (?,?,?,?,?,?)", tableName);
        var pst = connection.prepareStatement(st, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, model.getTitle());
        pst.setString(2, model.getAuthor());
        pst.setString(3, model.getContent());
        pst.setLong(4, model.getPubDate().toEpochDay());
        pst.setString(5, model.getStatus().toString());
        if (Objects.nonNull(model.getBorrowDate())) {
            pst.setLong(6, model.getBorrowDate().toEpochDay());
        } else {
            pst.setNull(6, java.sql.Types.BIGINT);
        }
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            long generatedId = rs.getLong(1);
            model.setId(generatedId);
        }

        return model;
    }

    private Book update(Book model) throws SQLException {
        var st = MessageFormat.format("UPDATE {0} SET title=?, author=?, content=?, pub_date=?, status=?, borrow_date=? WHERE id=?", tableName);
        var pst = connection.prepareStatement(st, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, model.getTitle());
        pst.setString(2, model.getAuthor());
        pst.setString(3, model.getContent());
        pst.setLong(4, model.getPubDate().toEpochDay());
        pst.setString(5, model.getStatus().toString());
        if (Objects.nonNull(model.getBorrowDate())) {
            pst.setLong(6, model.getBorrowDate().toEpochDay());
        } else {
            pst.setNull(6, java.sql.Types.BIGINT);
        }
        pst.setLong(7, model.getId());
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            long generatedId = rs.getLong(1);
            model.setId(generatedId);
        }

        return model;
    }


    @Override
    public Book getOne(Long id) throws SQLException, ItemNotFoundException {
        return super.getOne(id, Book.class);
    }
}
