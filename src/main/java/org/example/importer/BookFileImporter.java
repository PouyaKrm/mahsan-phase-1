package org.example.importer;

import org.example.library.Book;

import java.io.*;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.RecursiveTask;

public class BookFileImporter implements BookImporter {

    private Book[] books;
    private final Path filePath;
    private final String delimeter;
    private final String dateFormat;

    public BookFileImporter(Path filePath, String delimeter, String dateFormat) {
        this.filePath = filePath;
        this.delimeter = delimeter;
        this.dateFormat = dateFormat;
    }



    @Override
    public Book[] getBooks() throws FileNotFoundException {
        if (!Objects.isNull(books) && books.length > 0) {
            return (Book[]) Arrays.stream(books).toArray();
        }
        books = importBooks();
        var  copy  = new Book[books.length];
        Arrays.stream(books).toList().toArray(copy);
        return copy;
    }

    private Book[] importBooks() throws FileNotFoundException {
        List<Book> books = new ArrayList<>();
        var fileReader = new FileReader(new File(filePath.toUri()));

        try(var scanner = new Scanner(fileReader)) {
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                var book = createBook(line);
                books.add(book);
            }
        }

        Book[] bookArray = new Book[books.size()];
        books.toArray(bookArray);
        return bookArray;
    }

    private Book createBook(String line) {
        line.trim();
        String[] fields = line.split(delimeter);
        if (fields.length != 4) {
            throw new InvalidParameterException("Invalid book line: " + line);
        }

        var dateField = fields[2];
        var date = LocalDate.parse(dateField, DateTimeFormatter.ofPattern(dateFormat));
        return new Book(fields[0], fields[1], date, Enum.valueOf(Book.Status.class, fields[3]));
    }
}
