package org.example.library.model.library.book;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.library.AbstractLibraryRepository;
import org.example.library.model.BaseModel;
import org.example.library.model.DBFieldMapping;
import org.example.sql.JdbcConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BookRepositoryImpl extends AbstractLibraryRepository<Book> implements BookRepository {


    private static BookRepositoryImpl instance;


    protected BookRepositoryImpl(Connection connection) {
        super("books", connection, createFieldMappings());
    }

    private static Map<String, DBFieldMapping<BaseModel>> createFieldMappings() {
        Map<String, DBFieldMapping<BaseModel>> fieldMappings = new HashMap<>();
        fieldMappings.put("status",
                new DBFieldMapping<>(
                        "status",
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
}
