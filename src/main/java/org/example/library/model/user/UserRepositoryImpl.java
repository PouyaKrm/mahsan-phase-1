package org.example.library.model.user;

import org.example.exception.ItemNotFoundException;
import org.example.library.dto.UserBorrows;
import org.example.library.model.AbstractModelRepository;
import org.example.library.model.DBFieldMapping;
import org.example.library.model.borrow.BorrowModel;
import org.example.library.model.borrow.BorrowRepository;
import org.example.library.model.borrow.BorrowRepositoryImpl;
import org.example.library.model.borrow.BorrowTable;
import org.example.library.model.library.ModelAbstractFactory;
import org.example.library.model.library.book.Book;
import org.example.library.model.library.book.BookRepository;
import org.example.library.model.library.book.BookRepositoryImpl;
import org.example.utils.Utils;

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
                    DBFieldMapping.<User>builder().
                            tableName(TABLE_NAME)
                            .dbFieldName("name")
                            .definition("VARCHAR(40) NOT NULL")
                            .fromDB(User::setName)
                            .toDB(User::getName)
                            .dbType(Types.VARCHAR)
                            .build()
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

    @Override
    public UserBorrows[] getUserBorrowsCount() throws SQLException {
        var userId = borrowRepository.getFieldMappingMap().get(BorrowModel.USER_ID_FIELD_NAME);
        var id = getFieldMappingMap().get("id");
        final var countColumn = "user_count";
        var str = new StringBuilder()
                .append("select count(").append(userId.getDbFieldNameDotted()).append(") as ").append(countColumn).append(", ").append(getAllColumnsSelectLabel())
                .append(" from ").append(tableName)
                .append(" left join ")
                .append(BorrowTable.TABLE_NAME)
                .append(" on ")
                .append(id.getDbFieldNameDotted())
                .append(" = ")
                .append(userId.getDbFieldNameDotted())
                .append(" group by ")
                .append(getFieldMappings().stream().map(DBFieldMapping::getColumnLabel).collect(Collectors.joining(", ")))
                .append(" order by ")
                .append(countColumn)
                .toString();
        var result = connection.prepareStatement(str).executeQuery();
        List<UserBorrows> borrows = new ArrayList<>();
        while (result.next()) {
            var user = ModelAbstractFactory.getInstance().getDefaultFactory(User.class).populateFromDB(new User(), result, getFieldMappings());
            var count = result.getLong(1);
            var borrow = new UserBorrows(count, user);
            borrows.add(borrow);
        }
        return Utils.listToArray(borrows, UserBorrows.class);
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
        var user = getAll()[0];
        var books = getUserBooks(user.getId());
        user.getBorrowedBooks().addAll(books);
        return user;
    }

    private List<Book> getUserBooks(Long userId) throws SQLException {
        var bookColumns = bookRepository.getFieldMappings().stream().map(field -> "b." + field.dbFieldName() + " AS " + field.getColumnLabel()).collect(Collectors.joining(", "));
        var st = connection.prepareStatement(MessageFormat.format("select {0} from {1} b join {2} bo on b.id=bo.book_id where bo.user_id=?", bookColumns, bookRepository.getTableName(), borrowRepository.getTableName()));
        st.setObject(1, userId);
        var result = st.executeQuery();
        List<Book> books = new ArrayList<>();
        var bookFactory = ModelAbstractFactory.getInstance().getDefaultFactory(Book.class);
        while (result.next()) {
            var b = bookFactory.populateFromDB(new Book(), result, bookRepository.getFieldMappings());
            books.add(b);
        }
        return books;
    }
}
