package org.example.library.model.library.book;

import org.example.exception.BaseException;
import org.example.exception.InvalidOperationException;
import org.example.exception.ItemNotFoundException;
import org.example.library.model.borrow.BorrowModel;
import org.example.library.model.borrow.BorrowRepository;
import org.example.library.model.borrow.BorrowRepositoryImpl;
import org.example.library.model.borrow.BorrowTable;
import org.example.library.model.library.AbstractLibraryRepository;
import org.example.library.model.BaseModel;
import org.example.library.model.DBFieldMapping;
import org.example.library.model.library.ModelAbstractFactory;
import org.example.library.model.library.book.dto.BorrowAggregate;
import org.example.sql.JdbcConnection;
import org.example.utils.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BookRepositoryImpl extends AbstractLibraryRepository<Book> implements BookRepository {


    private static BookRepositoryImpl instance;
    private static final String STATUS_FIELD_NAME = "status";
    private static final String TABLE_NAME = "books";
    private final BorrowRepository borrowRepository = BorrowRepositoryImpl.getInstance();

    protected BookRepositoryImpl(Connection connection) {
        super("books", connection, createFieldMappings());
    }

    private static Map<String, DBFieldMapping<BaseModel>> createFieldMappings() {
        Map<String, DBFieldMapping<BaseModel>> fieldMappings = new HashMap<>();
        var statusMaping = DBFieldMapping.builder()
                .dbFieldName(STATUS_FIELD_NAME)
                .definition("VARCHAR(20) NOT NULL")
                .tableName(TABLE_NAME)
                .dbType(Types.VARCHAR)
                .fromDB((BaseModel model, String val) -> ((Book) model).setStatus(Book.Status.valueOf(val)))
                .toDB(model -> ((Book) model).getStatus().toString()
                ).build();
        fieldMappings.put(STATUS_FIELD_NAME, statusMaping);
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

    @Override
    public Book returnBook(Long userId, Long bookId) throws SQLException, BaseException {
        connection.setAutoCommit(false);
        var builder = new StringBuilder();
        builder.append("select ").append(borrowRepository.getAllColumnsSelectLabel())
                .append(", ")
                .append(getAllColumnsSelectLabel())
                .append(" from ")
                .append(borrowRepository.getTableName())
                .append(" join ").append(tableName).append(" on ")
                .append(borrowRepository.getTableName()).append(".").append(BorrowTable.BOOK_ID)
                .append(" = ")
                .append(tableName).append(".").append(ID_COLUMN)
                .append(" where ")
                .append(BorrowTable.USER_ID).append(" = ?")
                .append(" and ")
                .append(BorrowTable.BOOK_ID).append(" =?");

        var st = connection.prepareStatement(builder.toString());
        st.setObject(1, userId);
        st.setObject(2, bookId);
        var result = st.executeQuery();
        if (!result.next()) {
            throw new ItemNotFoundException("borrowed book not found");
        }
        var borrow = ModelAbstractFactory.getInstance().getDefaultFactory(BorrowModel.class).populateFromDB(new BorrowModel(), result, borrowRepository.getFieldMappings());
        if (Objects.nonNull(borrow.getReturnedAt())) {
            throw new InvalidOperationException("book already returned");
        }
        borrow.setReturnedAt(LocalDate.now());
        borrowRepository.save(borrow);
        var book = ModelAbstractFactory.getInstance().getFactory(Book.class).populateFromDB(new Book(), result, getFieldMappings());
        book.setStatus(Book.Status.EXIST);
        save(book);
        connection.commit();
        return book;
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

    @Override
    public BorrowAggregate[] getBorrowedBooksCount(int maxResult) throws SQLException {
        var max = maxResult > 0 ? maxResult : 3;
        var bookId = borrowRepository.getFieldMappingMap().get("bookId");
        var idField = getFieldMappingMap().get("id");
        final String countColumn = "count_result";
        var builder = new StringBuilder();
        builder
                .append("select ")
                .append("count(*) as ").append(countColumn)
                .append(", ")
                .append(getAllColumnsSelectLabel())
                .append(" from ")
                .append(BorrowTable.TABLE_NAME)
                .append(" join ")
                .append(tableName)
                .append(" on ")
                .append(bookId.getDbFieldNameDotted())
                .append(" = ")
                .append(idField.getDbFieldNameDotted())
                .append(" group by ").append(bookId.getDbFieldNameDotted())
                .append(" limit ?");

        var st = connection.prepareStatement(builder.toString());
        st.setObject(1, max);
        var result = st.executeQuery();
        List<BorrowAggregate> aggregates = new ArrayList<>();
        var f = ModelAbstractFactory.getInstance().getDefaultFactory(Book.class);
        while (result.next()) {
            var b = f.populateFromDB(new Book(), result, getFieldMappings());
            var a = new BorrowAggregate(result.getLong(1), b);
            aggregates.add(a);
        }
        return Utils.listToArray(aggregates, BorrowAggregate.class);
    }


}
