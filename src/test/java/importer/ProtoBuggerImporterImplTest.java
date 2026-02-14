package importer;

import org.example.importer.ProtoBuggerImporterImpl;
import org.example.library.model.article.Article;
import org.example.library.model.book.Book;
import org.example.library.model.magazine.Magazine;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;


public class ProtoBuggerImporterImplTest {

    @Test
    public void writes_books_correctly() throws IOException {
        Book[] books  = new Book[] {TestUtils.createBookWithId(), TestUtils.createBookWithId()};
        var importer = new ProtoBuggerImporterImpl();
        var path = ".";

        importer.writeToFile(books, Path.of(path), "out");

        assertThat(Files.exists(Path.of("out")));
        Files.deleteIfExists(Path.of("out"));
    }

    @Test
    public void writes_articles_correctly() throws IOException {
        Article[] books  = new Article[] {TestUtils.createArticleWithId(), TestUtils.createArticleWithId()};
        var importer = new ProtoBuggerImporterImpl();
        var path = ".";
        var fileName = "out_article";

        importer.writeToFile(books, Path.of(path), fileName);

        assertThat(Files.exists(Path.of(fileName)));
        Files.deleteIfExists(Path.of(fileName));
    }


  @Test
    public void writes_magazine_correctly() throws IOException {
        Magazine[] books  = new Magazine[] {TestUtils.createMagazineWithId(), TestUtils.createMagazineWithId()};
        var importer = new ProtoBuggerImporterImpl();
        var path = ".";
        var fileName = "out_magazine";

        importer.writeToFile(books, Path.of(path), fileName);

        assertThat(Files.exists(Path.of(fileName)));
        Files.deleteIfExists(Path.of(fileName));
    }





}
