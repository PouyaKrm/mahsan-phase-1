package org.example.library.model.book;

import org.example.library.model.ModelFactory;

import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookFactory extends ModelFactory<Book> {
    private final String DATE_FORMAT = "dd-MM-yyyy";
    private final String DELIMETER = ",";
    private static final BookFactory factory = new BookFactory();

    private BookFactory() {

    }

    public static BookFactory getFactory() {
        return factory;
    }

    @Override
    public Book createModelFromString(String string) {
        var str = string.trim();
        String[] fields = str.split(DELIMETER);
        if (fields.length != 5) {
            throw new InvalidParameterException("Invalid book line: " + str);
        }
        var dateField = fields[2];
        var date = LocalDate.parse(dateField, DateTimeFormatter.ofPattern(DATE_FORMAT));
        return new Book(date, fields[0], fields[1], fields[3], Enum.valueOf(Book.Status.class, fields[4]));
    }

    @Override
    public String parseModelToString(Book model) {
        return MessageFormat.format("{0},{1},{2},{3},{4}", model.getTitle(), model.getAuthor(), model.getPubDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)).toString(), model.getContent(), model.getStatus());
    }
}
