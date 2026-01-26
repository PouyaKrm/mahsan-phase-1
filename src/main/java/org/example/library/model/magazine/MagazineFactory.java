package org.example.library.model.magazine;

import org.example.library.model.ModelFactory;
import org.example.library.model.book.Book;
import org.example.library.model.book.BookFactory;

import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MagazineFactory extends ModelFactory<Magazine> {
    private final String DATE_FORMAT = "dd-MM-yyyy";
    private final String DELIMETER = ",";
    private static final MagazineFactory factory = new MagazineFactory();

    private MagazineFactory() {

    }

    public static MagazineFactory getFactory() {
        return factory;
    }

    @Override
    public Magazine createModelFromString(String string) {
        var str = string.trim();
        String[] fields = str.split(DELIMETER);
        if (fields.length != 5) {
            throw new InvalidParameterException("Invalid book line: " + str);
        }
        var dateField = fields[2];
        var date = LocalDate.parse(dateField, DateTimeFormatter.ofPattern(DATE_FORMAT));
        return new Magazine(date, fields[0], fields[1], fields[3]);
    }

    @Override
    public String parseModelToString(Magazine model) {
        return MessageFormat.format("{0},{1},{2},{3},{4}", model.getTitle(), model.getAuthor(), model.getPubDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)).toString(), model.getContent());
    }
}
