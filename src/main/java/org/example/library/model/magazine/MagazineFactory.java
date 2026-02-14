package org.example.library.model.magazine;

import org.example.exception.InvalidInputData;
import org.example.library.model.article.Article;
import org.example.library.v1.ArticleList;
import org.example.library.v1.MagazineList;
import org.example.library.validator.ModelDataValidator;
import org.example.library.validator.ModelDataValidatorImpl;
import org.example.library.model.AbstractModelFactory;
import org.example.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MagazineFactory extends AbstractModelFactory<Magazine> {
    private final String DATE_FORMAT = "dd-MM-yyyy";
    private final String DELIMETER = ",";
    private static final MagazineFactory factory = new MagazineFactory();
    private final ModelDataValidator validator = ModelDataValidatorImpl.getInstance();

    private MagazineFactory() {

    }

    @Override
    public Object createProtoBuffObject(Magazine magazine) {
        var builder = org.example.library.v1.Magazine.newBuilder()
                .setTitle(magazine.getTitle())
                .setAuthor(magazine.getAuthor())
                .setContent(magazine.getContent())
                .setId(magazine.getId())
                .setPubDateEpochDay(magazine.getPubDateEpochDay());
        if(Objects.nonNull(magazine.getBorrowDateEpochDay())) {
            builder.setBorrowDateEpochDay(magazine.getBorrowDateEpochDay());
        }
        return builder.build();
    }

    @Override
    public Object createProtoBuffList(Magazine[] items) {
        var builder = org.example.library.v1.MagazineList.newBuilder();
        for (var book : items) {
            var obj = createProtoBuffObject(book);
            builder.addMagazine((org.example.library.v1.Magazine) obj);
        }
        return builder.build();
    }

    @Override
    public Magazine[] parseProtoBuffObject(InputStream protoBuffObject) throws IOException {
        var protoList = MagazineList.parseFrom(protoBuffObject);
        var list = protoList.getMagazineList().stream().map(item -> {
            var b = new Magazine();
            b.setId(item.getId());
            b.setTitle(item.getTitle());
            b.setAuthor(item.getAuthor());
            b.setContent(item.getContent());
            b.setPubDateFromEpochDay(item.getPubDateEpochDay());
            return b;
        }).toList();
        return Utils.listToArray(list, Magazine.class);
    }

    public static MagazineFactory getFactory() {
        return factory;
    }

    @Override
    public Magazine createModelFromString(String string) throws InvalidInputData {
        var str = string.trim();
        String[] fields = str.split(DELIMETER);
        if (fields.length != 4) {
            throw new InvalidInputData("Invalid book line: " + str);
        }
        var dateField = fields[2];
        validator.validateDate(dateField, DATE_FORMAT);
        var date = LocalDate.parse(dateField, DateTimeFormatter.ofPattern(DATE_FORMAT));
        return new Magazine(date, fields[0], fields[1], fields[3]);
    }

    @Override
    public String parseModelToString(Magazine model) {
        return MessageFormat.format("{0},{1},{2},{3},{4}", model.getTitle(), model.getAuthor(), model.getPubDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)).toString(), model.getContent());
    }

    @Override
    public String getDelimeter() {
        return DELIMETER;
    }

    @Override
    public Magazine createFromResultSet(ResultSet rs) throws SQLException {
        var date = rs.getInt("pub_date");
        var pubDate = LocalDate.ofEpochDay(date);
        Long id = rs.getLong("id");
        var book = new Magazine(
                pubDate,
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("content")
        );

        book.setId(id);
        return book;
    }
}
