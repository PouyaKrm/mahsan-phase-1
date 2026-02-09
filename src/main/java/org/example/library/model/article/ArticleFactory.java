package org.example.library.model.article;

import org.example.exception.InvalidInputData;
import org.example.library.model.DBFieldMapping;
import org.example.library.validator.ModelDataValidator;
import org.example.library.validator.ModelDataValidatorImpl;
import org.example.library.model.AbstractModelFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ArticleFactory extends AbstractModelFactory<Article> {
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

    @Override
    public String getDelimeter() {
        return DELIMETER;
    }

    @Override
    public Article createFromResultSet(ResultSet rs) throws SQLException {

        Long id = rs.getLong("id");
        var book = new Article(
                getDate(rs, "pub_date"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("content")
        );
        book.setBorrowDate(getDate(rs, "borrow_date"));
        book.setId(id);
        return book;
    }



    private LocalDate getDate(ResultSet resultSet, String key) throws SQLException {
        var d = resultSet.getInt(key);
        return LocalDate.ofEpochDay(d);
    }
}
