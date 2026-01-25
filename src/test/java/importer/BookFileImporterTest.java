package importer;

import org.example.importer.BookImporterImpl;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.Scanner;

public class BookFileImporterTest {


    @Test
    public void import_books_successfully() throws FileNotFoundException {
        var fileName = "src/test/resources/book_file.txt";
        var path = Path.of(fileName);
        var fielReader = new FileReader(path.toFile());
        var scanner = new Scanner(fielReader);
        var importer = new BookImporterImpl();

        var books = importer.getBooks(scanner, ",", "dd-MM-yyyy");

        Assert.assertEquals(3, books.length);
    }

}
