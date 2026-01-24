package importer;

import org.example.importer.BookFileImporter;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.nio.file.Path;

public class BookFileImporterTest {

    @Test(expected = FileNotFoundException.class)
    public void throws_error_on_invalid_file_path() throws FileNotFoundException {
        var fileName = "fake";
        BookFileImporter importer = new BookFileImporter(Path.of(fileName), ",", "dd-MM-yyyy");

        importer.getBooks();
    }

    @Test
    public void import_books_successfully() throws FileNotFoundException {
        var fileName = "src/test/resources/book_file.txt";
        BookFileImporter importer = new BookFileImporter(Path.of(fileName), ",", "dd-MM-yyyy");

        var books = importer.getBooks();

        Assert.assertEquals(3, books.length);
    }

}
