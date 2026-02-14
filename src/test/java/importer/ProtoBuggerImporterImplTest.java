package importer;

import org.example.importer.ProtoBuggerImporterImpl;
import org.example.library.model.article.Article;
import org.example.library.model.book.Book;
import org.example.library.model.magazine.Magazine;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;


public class ProtoBuggerImporterImplTest {

    @Test
    public void writes_books_correctly() throws IOException {
        Book[] books  = new Book[] {TestUtils.createBookWithId(), TestUtils.createBookWithId()};
        var importer = new ProtoBuggerImporterImpl();
        var path = ".";

        importer.writeToFile(books, Path.of(path), "book_proto");

        assertThat(Files.exists(Path.of("book_proto")));
        Files.deleteIfExists(Path.of("out"));
    }

    @Test
    public void read_books_correctly() throws IOException {
        var importer = new ProtoBuggerImporterImpl();
        var file = new FileInputStream(Path.of("src/test/resources/book_proto").toFile());

        var books = importer.getModels(file, Book.class);

        Assert.assertEquals(2, books.length);
        assertThat(books).allMatch(item -> {
            return Objects.nonNull(item.getTitle()) &&
            Objects.nonNull(item.getId()) &&
            Objects.nonNull(item.getContent()) &&
            Objects.nonNull(item.getAuthor()) &&
            Objects.nonNull(item.getPubDate()) &&
            Objects.nonNull(item.getStatus());
        });
    }


    @Test
    public void writes_articles_correctly() throws IOException {
        Article[] books  = new Article[] {TestUtils.createArticleWithId(), TestUtils.createArticleWithId()};
        var importer = new ProtoBuggerImporterImpl();
        var path = ".";
        var fileName = "article_proto";

        importer.writeToFile(books, Path.of(path), fileName);

        assertThat(Files.exists(Path.of(fileName)));
        Files.deleteIfExists(Path.of(fileName));
    }

    @Test
    public void read_articles_correctly() throws IOException {
        var importer = new ProtoBuggerImporterImpl();
        var file = new FileInputStream(Path.of("src/test/resources/article_proto").toFile());

        var articles = importer.getModels(file, Article.class);

        Assert.assertEquals(2, articles.length);
        assertThat(articles).allMatch(item -> {
            return Objects.nonNull(item.getTitle()) &&
                    Objects.nonNull(item.getId()) &&
                    Objects.nonNull(item.getContent()) &&
                    Objects.nonNull(item.getAuthor()) &&
                    Objects.nonNull(item.getPubDate());
        });
    }



  @Test
    public void writes_magazine_correctly() throws IOException {
        Magazine[] books  = new Magazine[] {TestUtils.createMagazineWithId(), TestUtils.createMagazineWithId()};
        var importer = new ProtoBuggerImporterImpl();
        var path = ".";
        var fileName = "magazine_proto";

        importer.writeToFile(books, Path.of(path), fileName);

        assertThat(Files.exists(Path.of(fileName)));
        Files.deleteIfExists(Path.of(fileName));
    }


    @Test
    public void read_magazines_correctly() throws IOException {
        var importer = new ProtoBuggerImporterImpl();
        var file = new FileInputStream(Path.of("src/test/resources/magazine_proto").toFile());

        var magazines = importer.getModels(file, Magazine.class);

        Assert.assertEquals(2, magazines.length);
        assertThat(magazines).allMatch(item -> {
            return Objects.nonNull(item.getTitle()) &&
                    Objects.nonNull(item.getId()) &&
                    Objects.nonNull(item.getContent()) &&
                    Objects.nonNull(item.getAuthor()) &&
                    Objects.nonNull(item.getPubDate());
        });
    }


}
