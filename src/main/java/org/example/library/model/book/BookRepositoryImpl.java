package org.example.library.model.book;

import org.example.exception.ItemNotFoundException;
import org.example.library.AbstractModelRepository;
import org.example.library.model.ModelRepository;
import org.example.sql.JdbcConnection;
import org.example.utils.Utils;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookRepositoryImpl extends AbstractModelRepository<Book> implements BookRepository {

    private final Connection connection = JdbcConnection.getConnection();
    private final BookFactory factory = BookFactory.getFactory();
    private static BookRepositoryImpl instance;
    private final static String TABLE_NAME = "Books";
    private final static String ID_COLUMN = "id";
    private final static String TITLE_COLUMN = "title";
    private final static String AUTHOR_COLUMN = "author";
    private final static String PUB_DATE = "pub_date";
    private final static String BORROW_DATE_COLUMN = "borrow_date";
    private final static String CONTENT_COLUMN = "content";
    private final static String STATUS_COLUMN = "status";

    private BookRepositoryImpl() {

    }

    protected void createTable() throws SQLException {
        var createTableStatement = """
                CREATE TABLE IF NOT EXISTS {0} (
                    {1} INT NOT NULL AUTO_INCREMENT,
                    {2} VARCHAR(100) NOT NULL,
                    {3} VARCHAR(100) NOT NULL,
                    {4} TEXT NOT NULL,
                    {5} INT UNSIGNED NOT NULL,
                    {6} INT UNSIGNED,
                    {7} VARCHAR(20) NOT NULL,
                    CONSTRAINT PK_Book PRIMARY KEY (ID)
                );
                """;
        var statement = MessageFormat.format(
                createTableStatement,
                TABLE_NAME, ID_COLUMN, TITLE_COLUMN, AUTHOR_COLUMN, CONTENT_COLUMN, PUB_DATE, BORROW_DATE_COLUMN,
                STATUS_COLUMN
        );
        var st = connection.createStatement();
        st.execute(statement);
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
        List<Book> bookList = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement("select * from " + TABLE_NAME);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            bookList.add(factory.createFromResultSet(rs));
        }
        return Utils.listToArray(bookList, Book.class);
    }

    @Override
    public Book[] removeAll() throws SQLException {
        var ts = getAll();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM " + TABLE_NAME);
        ps.executeUpdate();
        return ts;
    }

    @Override
    public Book save(Book model) throws SQLException {
        return Objects.isNull(model.getId()) ? insertInto(model) : update(model);
    }

    private Book insertInto(Book model) throws SQLException {
        var st = MessageFormat.format("INSERT INTO {0} (title, author, content, pub_date, status, borrow_date) VALUES (?,?,?,?,?,?)", TABLE_NAME);
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
        var st = MessageFormat.format("UPDATE {0} SET title=?, author=?, content=?, pub_date=?, status=?, borrow_date=? WHERE id=?", TABLE_NAME);
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
    public boolean removeOne(Book model) throws SQLException {
        var s = MessageFormat.format("DELETE FROM {0} WHERE id = ?", TABLE_NAME);
        var pst = connection.prepareStatement(s);
        pst.setLong(1, model.getId());
        return pst.executeUpdate() > 0;
    }

    @Override
    public boolean removeOne(Long id) throws SQLException {
        var s = MessageFormat.format("DELETE FROM {0} WHERE id = ?", TABLE_NAME);
        var pst = connection.prepareStatement(s);
        pst.setLong(1, id);
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
