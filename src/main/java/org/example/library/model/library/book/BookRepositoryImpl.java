package org.example.library.model.library.book;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.borrow.BorrowRepository;
import org.example.library.model.borrow.BorrowRepositoryImpl;
import org.example.library.model.library.AbstractLibraryRepository;
import org.example.library.model.BaseModel;
import org.example.library.model.DBFieldMapping;
import org.example.library.model.library.ModelAbstractFactory;
import org.example.sql.JdbcConnection;
import org.example.utils.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.*;

public class BookRepositoryImpl extends AbstractLibraryRepository<Book> implements BookRepository {


    private static BookRepositoryImpl instance;
    private static final String STATUS_FIELD_NAME = "status";
    private final BorrowRepository borrowRepository = BorrowRepositoryImpl.getInstance();

    protected BookRepositoryImpl(Connection connection) {
        super("books", connection, createFieldMappings());
    }

    private static Map<String, DBFieldMapping<BaseModel>> createFieldMappings() {
        Map<String, DBFieldMapping<BaseModel>> fieldMappings = new HashMap<>();
        fieldMappings.put(STATUS_FIELD_NAME,
                new DBFieldMapping<>(
                        STATUS_FIELD_NAME,
                        "VARCHAR(20) NOT NULL",
                        (BaseModel model, String val) -> ((Book) model).setStatus(Book.Status.valueOf(val)),
                        model -> ((Book) model).getStatus().toString(), Types.VARCHAR)
        );
        return fieldMappings;
    }



    public synchronized static BookRepositoryImpl getInstance(Connection connection) {
        if (Objects.nonNull(instance)) {
            return instance;
        }

        try {
            instance = new BookRepositoryImpl(connection);
            instance.createTable();
            return instance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static BookRepositoryImpl getInstance() {
        return getInstance(JdbcConnection.getConnection());
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
    public Book getOne(Long id) throws SQLException, ItemNotFoundException {
        return super.getOne(id, Book.class);
    }

    @Override
    public Book[] getAllByStatus(Book.Status status) throws SQLException {
        var field = getFieldMappingMap().get(STATUS_FIELD_NAME);
        var st = connection.prepareStatement(MessageFormat.format("select * from {0} where {1} = ?", tableName, field.dbFieldName()));
        st.setString(1, status.name());
        var result = st.executeQuery();
        List<Book> books = new ArrayList<>();
        var f = ModelAbstractFactory.getInstance().getDefaultFactory(Book.class);
        while (result.next()) {
            var b = f.populateFromDB(new Book(), result, getFieldMappings());
            books.add(b);
        }
        return Utils.listToArray(books, Book.class);
    }

    @Override
    public Book[] getNonBorrowedBooksAtAll() throws SQLException {
        var st = connection.prepareStatement(MessageFormat.format("select * from {0} left join {1} on {0}.id={1}.book_id where {1}.book_id is NULL", tableName, borrowRepository.getTableName()));
        var result = st.executeQuery();
        return createAllFromResultSet(result);
    }

    private Book[] createAllFromResultSet(ResultSet result) throws SQLException {
        List<Book> books = new ArrayList<>();
        var f = ModelAbstractFactory.getInstance().getDefaultFactory(Book.class);
        while (result.next()) {
            var b = f.populateFromDB(new Book(), result, getFieldMappings());
            books.add(b);
        }
        return Utils.listToArray(books, Book.class);
    }
}
