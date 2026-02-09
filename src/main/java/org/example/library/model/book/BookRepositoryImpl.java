package org.example.library.model.book;

import org.example.exception.ItemNotFoundException;
import org.example.library.AbstractModelRepository;
import org.example.library.model.BaseModel;
import org.example.library.model.DBFieldMapping;

import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BookRepositoryImpl extends AbstractModelRepository<Book> implements BookRepository {


    private static BookRepositoryImpl instance;


    protected BookRepositoryImpl() {
        super("books", createFieldMappings());
    }

    private static Map<String, DBFieldMapping> createFieldMappings() {
        Map<String, DBFieldMapping> fieldMappings = new HashMap<>();
        fieldMappings.put("status",
                new DBFieldMapping(
                        "status",
                        "VARCHAR(20) NOT NULL",
                        (BaseModel model, String value) -> ((Book) model).setStatus(Book.Status.valueOf(value)),
                        model -> ((Book) model).getStatus().toString(), Types.VARCHAR)
        );
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
    public Book getOne(Long id) throws SQLException, ItemNotFoundException {
        return super.getOne(id, Book.class);
    }
}
