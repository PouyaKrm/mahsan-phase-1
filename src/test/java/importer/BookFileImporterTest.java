package importer;

import org.example.constansts.ResourceType;
import org.example.importer.BookImporterImpl;
import org.example.library.model.book.Book;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.util.Scanner;

public class BookFileImporterTest {


    @Test
    public void import_books_successfully() throws IOException {
        var fileName = "src/test/resources/book_file.txt";
        var stream = new FileInputStream(fileName);
        var importer = new BookImporterImpl();

        var books = importer.getModels(stream, ResourceType.BOOK, Book.class);

        Assert.assertEquals(3, books.length);
    }

}
