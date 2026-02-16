package org.example.library.model.user;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.AbstractModelRepository;
import org.example.library.model.DBFieldMapping;
import org.example.library.model.borrow.BorrowModel;
import org.example.library.model.borrow.BorrowRepository;
import org.example.library.model.borrow.BorrowRepositoryImpl;
import org.example.library.model.library.ModelAbstractFactory;
import org.example.library.model.library.book.Book;
import org.example.library.model.library.book.BookRepository;
import org.example.library.model.library.book.BookRepositoryImpl;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;


public class UserRepositoryImpl extends AbstractModelRepository<User> implements UserRepository {

    private final BorrowRepository borrowRepository = BorrowRepositoryImpl.getInstance();
    private final BookRepository bookRepository = BookRepositoryImpl.getInstance();
    private static UserRepositoryImpl instance;
    private static final String TABLE_NAME = "users";
    private static final Map<String, DBFieldMapping> FIELD_MAPPING = Map.ofEntries(
            Map.entry("name",
                    new DBFieldMapping<>(
                            "name",
                            "VARCHAR(40) NOT NULL",
                            User::setName,
                            User::getName,
                            Types.VARCHAR
                    )
            )
    );

    @Override
    public User save(User model) throws SQLException {
        var u = super.save(model);
        u.getBorrowedBooks().addAll(getUserBooks(u.getId()));
        return u;
    }

    protected UserRepositoryImpl() {
        super(TABLE_NAME, FIELD_MAPPING);
    }

    @Override
    public User[] getAll() throws SQLException {
        return getAll(User.class);
    }

    @Override
    public User[] removeAll() throws SQLException {
        return removeAll(User.class);
    }

    @Override
    public User getOne(Long id) throws SQLException, ItemNotFoundException {
        var u = getOne(id, User.class);
        var bs = getUserBooks(u.getId());
        u.getBorrowedBooks().addAll(bs);
        return u;
    }

    public static synchronized UserRepositoryImpl getInstance() {
        if (Objects.nonNull(instance)) {
            return instance;
        }
        instance = new UserRepositoryImpl();
        try {
            instance.createTable();
            return instance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByName(String name) {
        Optional<User> user = Optional.empty();
        try {
            var st = connection.prepareStatement(MessageFormat.format("select * from {0} where name=?", TABLE_NAME));
            var result = st.executeQuery();
            st.setObject(1, name);
            while (result.next()) {
                if (user.isPresent()) {
                    throw new IllegalStateException("query returned more than one result");
                }
                var f = ModelAbstractFactory.getInstance().getDefaultFactory(User.class);
                var u = f.populateFromDB(new User(), result, getFieldMappings());
                user = Optional.of(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public User getDefaultUser() throws SQLException {
        var bookColumns = bookRepository.getFieldMappings().stream().map(field -> "b." + field.dbFieldName() + " AS " + field.getColumnLabel(bookRepository.getTableName())).collect(Collectors.joining(", "));
        var userColumns = getFieldMappings().stream().map(field -> "u." + field.dbFieldName() + " AS " + field.getColumnLabel(getTableName())).collect(Collectors.joining(", "));
        var st = connection.prepareStatement(MessageFormat.format("select {0}, {1} from {2} u join {3} bo on u.id=bo.user_id join {4} b on bo.book_id=b.id order by u.id", userColumns, bookColumns, TABLE_NAME, borrowRepository.getTableName(), bookRepository.getTableName()));
        var result = st.executeQuery();
        Set<User> users = new HashSet<>();
        if (!result.next()) {
            throw new IllegalStateException("query returned no data");
        }
        var userFactory = ModelAbstractFactory.getInstance().getDefaultFactory(User.class);
        var u = userFactory.populateFromDB(new User(), result, getFieldMappings(), getTableName());
        var books = getUserBooks(u.getId());
        u.getBorrowedBooks().addAll(books);
        return u;
    }

    private List<Book> getUserBooks(Long userId) throws SQLException {
        var bookColumns = bookRepository.getFieldMappings().stream().map(field -> "b." + field.dbFieldName() + " AS " + field.getColumnLabel(bookRepository.getTableName())).collect(Collectors.joining(", "));
        var st = connection.prepareStatement(MessageFormat.format("select {0} from {1} b join {2} bo on b.id=bo.book_id where bo.user_id=?", bookColumns, bookRepository.getTableName(), borrowRepository.getTableName()));
        st.setObject(1, userId);
        var result = st.executeQuery();
        List<Book> books = new ArrayList<>();
        var bookFactory = ModelAbstractFactory.getInstance().getDefaultFactory(Book.class);
        while (result.next()) {
            var b = bookFactory.populateFromDB(new Book(), result, bookRepository.getFieldMappings(), bookRepository.getTableName());
            books.add(b);
        }
        return books;
    }
}
