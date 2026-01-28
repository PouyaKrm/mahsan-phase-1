package org.example.library.model.book;

import org.example.exception.InvalidInputData;
import org.example.library.validator.ModelDataValidator;
import org.example.library.validator.ModelDataValidatorImpl;
import org.example.library.model.AbstractModelFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class BookFactory extends AbstractModelFactory<Book> {
    private final String DATE_FORMAT = "dd-MM-yyyy";
    private final String DELIMETER = ",";
    private static final BookFactory factory = new BookFactory();
    private final ModelDataValidator validator = ModelDataValidatorImpl.getInstance();

    private BookFactory() {

    }

    public static BookFactory getFactory() {
        return factory;
    }

    @Override
    public Book createModelFromString(String string) throws InvalidInputData {
        var str = string.trim();
        String[] fields = str.split(DELIMETER);
        if (fields.length != 5) {
            throw new InvalidInputData("Invalid book line: " + str);
        }
        var dateField = fields[2];
        validator.validateDate(dateField, DATE_FORMAT);
        var date = LocalDate.parse(dateField, DateTimeFormatter.ofPattern(DATE_FORMAT));
        return new Book(date, fields[0], fields[1], fields[3], Enum.valueOf(Book.Status.class, fields[4]));
    }

    @Override
    public Book createFromResultSet(ResultSet rs) throws SQLException {
        var date = rs.getInt("date");
        var pubDate = LocalDate.ofEpochDay(date);
        var status = rs.getString("status");
        Book book = new Book(
                pubDate,
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("content"),
                Book.Status.valueOf(status)
        );

        var id = rs.getInt("id");
        book.setId(id);
        var bd = rs.getInt("borrow_date");
        if(Objects.nonNull(bd)) {
            var borrowDate = LocalDate.ofEpochDay(bd);
            book.setBorrowDate(borrowDate);
        }
        return book;
    }

    @Override
    public String parseModelToString(Book model) {
        return MessageFormat.format("{0},{1},{2},{3},{4}", model.getTitle(), model.getAuthor(), model.getPubDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)).toString(), model.getContent(), model.getStatus());
    }

    @Override
    public String getDelimeter() {
        return DELIMETER;
    }
}
