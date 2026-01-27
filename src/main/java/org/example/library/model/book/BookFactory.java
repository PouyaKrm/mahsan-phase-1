package org.example.library.model.book;

import org.example.exception.InvalidInputData;
import org.example.library.validator.ModelDataValidator;
import org.example.library.validator.ModelDataValidatorImpl;
import org.example.library.model.ModelFactory;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookFactory extends ModelFactory<Book> {
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
    public String parseModelToString(Book model) {
        return MessageFormat.format("{0},{1},{2},{3},{4}", model.getTitle(), model.getAuthor(), model.getPubDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)).toString(), model.getContent(), model.getStatus());
    }
}
