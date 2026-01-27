package org.example.library.model.article;

import org.example.exception.InvalidInputData;
import org.example.library.ModelDataValidator;
import org.example.library.ModelDataValidatorImpl;
import org.example.library.model.ModelFactory;
import org.example.library.model.book.Book;
import org.example.library.model.book.BookFactory;

import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ArticleFactory extends ModelFactory<Article> {
    private final String DATE_FORMAT = "dd-MM-yyyy";
    private final String DELIMETER = ",";
    private static final ArticleFactory factory = new ArticleFactory();
    private final ModelDataValidator validator = ModelDataValidatorImpl.getInstance();

    private ArticleFactory() {

    }

    public static ArticleFactory getFactory() {
        return factory;
    }

    @Override
    public Article createModelFromString(String string) throws InvalidInputData {
        var str = string.trim();
        String[] fields = str.split(DELIMETER);
        if (fields.length != 4) {
            throw new InvalidInputData("Invalid book line: " + str);
        }

        var dateField = fields[2];
        validator.validateDate(dateField, DATE_FORMAT);
        var date = LocalDate.parse(dateField, DateTimeFormatter.ofPattern(DATE_FORMAT));
        return new Article(date, fields[0], fields[1], fields[3]);
    }

    @Override
    public String parseModelToString(Article model) {
        return MessageFormat.format("{0},{1},{2},{3}", model.getTitle(), model.getAuthor(), model.getPubDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)).toString(), model.getContent());
    }
}
