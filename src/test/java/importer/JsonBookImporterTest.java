package importer;

import org.example.constansts.ResourceType;
import org.example.importer.BookImporterImpl;
import org.example.importer.JsonBookImporter;
import org.example.library.model.book.Book;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class JsonBookImporterTest {

    @Test
    public void imports_json_file_successfully() throws IOException {
        var fileName = "src/test/resources/book_file.json";
        var stream = new FileInputStream(fileName);
        var importer = new JsonBookImporter();

        var books = importer.getModels(stream, ResourceType.BOOK, Book.class);

        Assert.assertEquals(3, books.length);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getModels_by_terminationline_throws_not_supported_exception() throws IOException {
        var importer = new JsonBookImporter();

        var books = importer.getModels(System.in, ResourceType.BOOK, Book.class, "line");
    }

}
