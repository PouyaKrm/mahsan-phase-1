package org.example.library.model.library.article;

import org.example.exception.InvalidInputData;
import org.example.library.v1.ArticleList;
import org.example.library.validator.ModelDataValidator;
import org.example.library.validator.ModelDataValidatorImpl;
import org.example.library.model.library.AbstractLibraryModelFactory;
import org.example.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ArticleFactoryLibrary extends AbstractLibraryModelFactory<Article> {
    private final String DATE_FORMAT = "dd-MM-yyyy";
    private final String DELIMETER = ",";
    private static final ArticleFactoryLibrary factory = new ArticleFactoryLibrary();
    private final ModelDataValidator validator = ModelDataValidatorImpl.getInstance();

    private ArticleFactoryLibrary() {
    }

    @Override
    public Object createProtoBuffObject(Article article) {
        var builder = org.example.library.v1.Article.newBuilder()
                .setTitle(article.getTitle())
                .setAuthor(article.getAuthor())
                .setContent(article.getContent())
                .setId(article.getId())
                .setPubDateEpochDay(article.getPubDateEpochDay());
        if (Objects.nonNull(article.getBorrowDateEpochDay())) {
            builder.setBorrowDateEpochDay(article.getBorrowDateEpochDay());
        }
        return builder.build();
    }

    @Override
    public Object createProtoBuffList(Article[] items) {
        var builder = org.example.library.v1.ArticleList.newBuilder();
        for (var book : items) {
            var obj = createProtoBuffObject(book);
            builder.addArticle((org.example.library.v1.Article) obj);
        }
        return builder.build();
    }

    @Override
    public Article[] parseProtoBuffObject(InputStream protoBuffObject) throws IOException {
        var protoList = ArticleList.parseFrom(protoBuffObject);
        var list = protoList.getArticleList().stream().map(item -> {
            var b = new Article();
            b.setId(item.getId());
            b.setTitle(item.getTitle());
            b.setAuthor(item.getAuthor());
            b.setContent(item.getContent());
            b.setPubDateFromEpochDay(item.getPubDateEpochDay());
            return b;
        }).toList();
        return Utils.listToArray(list, Article.class);
    }

    public static ArticleFactoryLibrary getFactory() {
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


    private LocalDate getDate(ResultSet resultSet, String key) throws SQLException {
        var d = resultSet.getInt(key);
        return LocalDate.ofEpochDay(d);
    }
}
