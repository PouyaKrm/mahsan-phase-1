package org.example.library.model.library.book;

import org.example.exception.InvalidInputData;
import org.example.library.model.library.ModelAbstractFactory;
import org.example.library.v1.BookList;
import org.example.library.validator.ModelDataValidator;
import org.example.library.validator.ModelDataValidatorImpl;
import org.example.library.model.library.AbstractLibraryModelFactory;
import org.example.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class BookFactoryLibrary extends AbstractLibraryModelFactory<Book> {
    private final String DATE_FORMAT = "dd-MM-yyyy";
    private final String DELIMETER = ",";
    private static final BookFactoryLibrary factory = new BookFactoryLibrary();
    private final ModelDataValidator validator = ModelDataValidatorImpl.getInstance();

    private BookFactoryLibrary() {

    }

    @Override
    public Object createProtoBuffObject(Book book) {
        var builder = org.example.library.v1.Book.newBuilder()
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setContent(book.getContent())
                .setId(book.getId())
                .setStatus(org.example.library.v1.Book.Status.valueOf(book.getStatus().name()))
                .setPubDateEpochDay(book.getPubDateEpochDay());
        if (Objects.nonNull(book.getBorrowDate())) {
            builder.setBorrowDateEpochDay(book.getBorrowDateEpochDay());
        }
        return builder.build();
    }

    @Override
    public Object createProtoBuffList(Book[] items) {
        var builder = org.example.library.v1.BookList.newBuilder();
        for (var book : items) {
            var obj = createProtoBuffObject(book);
            builder.addBook((org.example.library.v1.Book) obj);
        }
        return builder.build();
    }

    @Override
    public Book[] parseProtoBuffObject(InputStream protoBuffObject) throws IOException {
        var protoList = BookList.parseFrom(protoBuffObject);
        var list = protoList.getBookList().stream().map(item -> {
            var b = new Book();
            b.setId(item.getId());
            b.setTitle(item.getTitle());
            b.setAuthor(item.getAuthor());
            b.setContent(item.getContent());
            b.setStatus(Book.Status.valueOf(item.getStatus().name()));
            b.setPubDateFromEpochDay(item.getPubDateEpochDay());
            b.setBorrowDateFromEpochDay(item.getBorrowDateEpochDay());
            return b;
        }).toList();
        return Utils.listToArray(list, Book.class);
    }

    public static BookFactoryLibrary getFactory() {
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

    @Override
    public String getDelimeter() {
        return DELIMETER;
    }
}
