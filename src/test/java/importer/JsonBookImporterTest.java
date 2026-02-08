package importer;

import org.example.importer.JsonBookImporterImpl;
import org.example.library.InMemoryLibraryImpl;
import org.example.library.model.book.Book;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

public class JsonBookImporterTest {

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
    public void imports_json_file_successfully() throws IOException {
        var fileName = "src/test/resources/books.txt";
        var stream = new FileInputStream(fileName);
        var importer = new JsonBookImporterImpl();

        var books = importer.getModels(stream, Book.class);

        Assert.assertEquals(6, books.length);
    }


    @Test
    public void write_to_file_works_successfully() throws IOException, SQLException {
        var library = new InMemoryLibraryImpl();
        var books = new Book[]{TestUtils.createBook(), TestUtils.createBook(), TestUtils.createBook()};
        library.addItem(books[0]);
        library.addItem(books[1]);
        library.addItem(books[2]);
        var importer = new JsonBookImporterImpl();
        var path = Path.of(testFolderPath);
        importer.writeToFile(library.getAll(), path, bookFileName);
        var stream = new FileInputStream(path.resolve(bookFileName).toFile());
        var data = importer.getModels(stream, Book.class);
        Assert.assertEquals(3, data.length);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getModels_by_terminationline_throws_not_supported_exception() throws IOException {
        var importer = new JsonBookImporterImpl();

        var books = importer.getModels(System.in, Book.class, "line");
    }


}
