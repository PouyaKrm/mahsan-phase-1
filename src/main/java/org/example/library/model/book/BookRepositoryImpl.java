package org.example.library.model.book;

import org.example.library.model.ModeRepository;
import org.example.sql.JdbcConnection;
import org.example.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements ModeRepository<Book> {

    private final Connection connection = JdbcConnection.getInstance().getConnection();

    private final static String TABLE_NAME = "book";

    @Override
    public Book[] getAll() throws SQLException {
        List<Book> bookList = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement("select * from " + TABLE_NAME);
        ResultSet rs = ps.executeQuery();
        var date = rs.getInt("date");
        var pubDate = LocalDate.ofEpochDay(date);
        var id = rs.getInt("id");
        var status = rs.getString("status");

        while (rs.next()) {
            Book book = new Book(
                    pubDate,
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("content"),
                    Book.Status.valueOf(status)
            );

            book.setId(id);

            bookList.add(book);
        }
        return Utils.listToArray(bookList, Book.class);
    }

    @Override
    public Book addOne(Book model) {
        return null;
    }

    @Override
    public void removeOne(Book model) {

    }

    @Override
    public Book getOne(int id) {
        return null;
    }
}
