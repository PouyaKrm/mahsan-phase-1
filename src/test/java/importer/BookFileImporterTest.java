package importer;

import org.example.importer.BookImporterImpl;
import org.example.library.InMemoryLibraryImpl;
import org.example.library.model.book.Book;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

public class BookFileImporterTest {

    private final String testFolderPath = "src/test/resources";
    private final String bookFileName = "test_book_file.txt";

    @After
    public void cleanUp() throws IOException {
        var path = Path.of(testFolderPath);
        path = path.resolve(bookFileName);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    @Test
    public void import_books_successfully() throws IOException {
        var fileName = "src/test/resources/book_file.txt";
        var stream = new FileInputStream(fileName);
        var importer = new BookImporterImpl();

        var books = importer.getModels(stream, Book.class);
        Assert.assertEquals(3, books.length);
        for (var book : books) {
            Assert.assertEquals(Book.class, book.getClass());
        }

    }

    @Test
    public void write_to_file_works_successfully() throws IOException, SQLException {
        var library = new InMemoryLibraryImpl();
        var books = new Book[]{TestUtils.createBook(), TestUtils.createBook(), TestUtils.createBook()};
        library.addItem(books[0]);
        library.addItem(books[1]);
        library.addItem(books[2]);
        var importer = new BookImporterImpl();
        var path = Path.of(testFolderPath);
        importer.writeToFile(library.getAll(), path, bookFileName);

        var stream = new FileInputStream(path.resolve(bookFileName).toFile());
        var data = importer.getModels(stream, Book.class);
        Assert.assertEquals(3, data.length);

    }

}
