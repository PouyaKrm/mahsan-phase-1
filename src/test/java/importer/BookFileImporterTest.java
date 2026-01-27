package importer;

import org.example.constansts.ResourceType;
import org.example.importer.BookImporterImpl;
import org.example.library.Library;
import org.example.library.model.book.Book;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

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

    public void write_to_file_works_successfully() throws IOException {
        var library = new Library();
        library.addItem(TestUtils.createBook());
        library.addItem(TestUtils.createArticle());
        library.addItem(TestUtils.createMagazine());
        var importer = new BookImporterImpl();
        var path = "src/test/resources/test_book_file.txt";

        importer.writeToFile(library.getAll(), Path.of(path));
        InputStream stream = new FileInputStream(path);
        var data = importer.getModels(stream, )

    }

}
