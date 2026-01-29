package org.example.library.model.book;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.ModelRepository;
import org.example.sql.JdbcConnection;
import org.example.utils.Utils;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookRepositoryImpl implements ModelRepository<Book> {

    private final Connection connection = JdbcConnection.getConnection();

    private final static String TABLE_NAME = "book";
    private final BookFactory factory = BookFactory.getFactory();

    @Override
    public Book[] getAll() throws SQLException {
        List<Book> bookList = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement("select * from " + TABLE_NAME);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            bookList.add(factory.createFromResultSet(rs));
        }
        return Utils.listToArray(bookList, Book.class);
    }

    @Override
    public Book addOne(Book model) throws SQLException {
        var st = MessageFormat.format("INSERT INTO {0} (title, author, content, pub_date, status, borrow_date) VALUES (?,?,?,?,?,?)", TABLE_NAME);
        var pst = connection.prepareStatement(st, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, model.getTitle());
        pst.setString(2, model.getAuthor());
        pst.setString(3, model.getContent());
        pst.setLong(4, model.getPubDate().toEpochDay());
        pst.setString(5, model.getStatus().toString());
        if(Objects.nonNull(model.getBorrowDate())) {
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

    @Override
    public boolean removeOne(Book model) throws SQLException {
        var s = MessageFormat.format("DELETE FROM {0} WHERE id = ?", TABLE_NAME);
        var pst = connection.prepareStatement(s);
        pst.setLong(1, model.getId());
        return pst.executeUpdate() > 0;
    }

    @Override
    public Book getOne(Long id) throws SQLException, ItemNotFoundException {
        var s = MessageFormat.format("SELECT * FROM {0} WHERE id = ?", TABLE_NAME);
        var pst = connection.prepareStatement(s);
        pst.setLong(1, id);
        var rs = pst.executeQuery();
        if (rs.next()) {
            return factory.createFromResultSet(rs);
        } else {
            throw new ItemNotFoundException(MessageFormat.format("No item found with id {0}", id));
        }
    }
}
