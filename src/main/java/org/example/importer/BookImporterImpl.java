package org.example.importer;

import org.example.library.model.Book;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class BookImporterImpl implements BookImporter {


    public Book createBook(String line, String delimeter, String dateFormat)  {
        line.trim();
        String[] fields = line.split(delimeter);
        if (fields.length != 4) {
            throw new InvalidParameterException("Invalid book line: " + line);
        }

        var dateField = fields[2];
        var date = LocalDate.parse(dateField, DateTimeFormatter.ofPattern(dateFormat));
        return new Book(date, fields[0], fields[1], Enum.valueOf(Book.Status.class, fields[3]));
    }

    @Override
    public Book[] getBooks(Scanner scanner, String delimeter, String dateFormat) {
       return getBooks(scanner, delimeter, dateFormat, Optional.empty());
    }

    @Override
    public Book[] getBooks(Scanner scanner, String delimeter, String dateFormat, String terminationLine) {
        return getBooks(scanner, delimeter, dateFormat, Optional.of(terminationLine));
    }


    private Book[] getBooks(Scanner scanner, String delimeter, String dateFormat, Optional<String> terminationLine) {
        List<Book> books = new ArrayList<>();
        while (scanner.hasNextLine()) {
            try {
                var line = scanner.nextLine();
                if (terminationLine.isPresent() && line.equals(terminationLine.get())) {
                    break;
                }
                var book = createBook(line, delimeter, dateFormat);
                books.add(book);

            } catch (InvalidParameterException e) {
                e.printStackTrace();
            }

        }

        Book[] bookArray = new Book[books.size()];
        books.toArray(bookArray);
        return bookArray;
    }
}
