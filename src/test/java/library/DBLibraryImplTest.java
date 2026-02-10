package library;

import org.example.exception.ItemNotFoundException;
import org.example.library.DbLibraryImpl;
import org.example.library.model.article.Article;
import org.example.library.model.article.ArticleRepositoryImpl;
import org.example.library.model.book.Book;
import org.example.library.model.book.BookRepositoryImpl;
import org.example.library.model.magazine.Magazine;
import org.example.library.model.magazine.MagazineRepositoryImpl;
import org.junit.After;
import org.junit.Test;
import utils.TestUtils;

import java.sql.SQLException;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DBLibraryImplTest {
    private final DbLibraryImpl dbLibrary = new DbLibraryImpl();
    private final MagazineRepositoryImpl magazineRepository = MagazineRepositoryImpl.getInstance();
    private final BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
    private final ArticleRepositoryImpl articleRepository = ArticleRepositoryImpl.getInstance();


    @After
    public void cleanUp() throws SQLException {
        magazineRepository.removeAll();
        bookRepository.removeAll();
        articleRepository.removeAll();
    }


    @Test
    public void getAll_books_works_correctly() {
        var items = new Book[]{TestUtils.createBook("title"), TestUtils.createBook("title2")};
        dbLibrary.addAll(items, Book.class);

        var result = dbLibrary.getAll(Book.class);

        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void getAll_magazines_works_correctly() {
        var items = new Magazine[]{TestUtils.createMagazine("title"), TestUtils.createMagazine("title2")};
        dbLibrary.addAll(items, Magazine.class);

        var result = dbLibrary.getAll(Magazine.class);

        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void getAll_articles_works_correctly() {
        var items = new Article[]{TestUtils.createArticle("title"), TestUtils.createArticle("title2")};
        dbLibrary.addAll(items, Article.class);

        var result = dbLibrary.getAll(Article.class);

        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void add_book_works_correctly() {
        var items = new Book[]{TestUtils.createBook("title"), TestUtils.createBook("title2")};

        Arrays.stream(items).forEach(item -> dbLibrary.addItem(item, Book.class));

        var result = dbLibrary.getAll(Book.class);
        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void add_article_works_correctly() {
        var items = new Article[]{TestUtils.createArticle("title"), TestUtils.createArticle("title2")};

        Arrays.stream(items).forEach(item -> dbLibrary.addItem(item, Article.class));

        var result = dbLibrary.getAll(Article.class);
        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void add_magazine_works_correctly() {
        var items = new Magazine[]{TestUtils.createMagazine("title"), TestUtils.createMagazine("title2")};

        Arrays.stream(items).forEach(item -> dbLibrary.addItem(item, Magazine.class));

        var result = dbLibrary.getAll(Magazine.class);
        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void addAll_books_works_correctly() {
        var items = new Book[]{TestUtils.createBook("title"), TestUtils.createBook("title2")};

        dbLibrary.addAll(items, Book.class);

        var result = dbLibrary.getAll(Book.class);
        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void addAll_articles_works_correctly() {
        var items = new Article[]{TestUtils.createArticle("title"), TestUtils.createArticle("title2")};

        dbLibrary.addAll(items, Article.class);

        var result = dbLibrary.getAll(Article.class);
        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void addAll_magazines_works_correctly() {
        var items = new Magazine[]{TestUtils.createMagazine("title"), TestUtils.createMagazine("title2")};

        dbLibrary.addAll(items, Magazine.class);

        var result = dbLibrary.getAll(Magazine.class);
        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }


    @Test
    public void remove_one_book_works_correctly() {
        var items = new Book[]{TestUtils.createBook("title"), TestUtils.createBook("title2")};
        var saved = dbLibrary.addAll(items, Book.class);

        dbLibrary.removeItem(saved[0], Book.class);

        var result = dbLibrary.getAll(Book.class);
        assertThat(result).hasSize(1);
        assertThat(result[0].getTitle()).isEqualTo(saved[1].getTitle());
    }

    @Test
    public void remove_one_article_works_correctly() {
        var items = new Article[]{TestUtils.createArticle("title"), TestUtils.createArticle("title2")};
        var saved = dbLibrary.addAll(items, Article.class);

        dbLibrary.removeItem(saved[0], Article.class);

        var result = dbLibrary.getAll(Article.class);
        assertThat(result).hasSize(1);
        assertThat(result[0].getTitle()).isEqualTo(saved[1].getTitle());
    }

    @Test
    public void remove_one_magazine_works_correctly() {
        var items = new Magazine[]{TestUtils.createMagazine("title"), TestUtils.createMagazine("title2")};
        var saved = dbLibrary.addAll(items, Magazine.class);

        dbLibrary.removeItem(saved[0], Magazine.class);

        var result = dbLibrary.getAll(Magazine.class);
        assertThat(result).hasSize(1);
        assertThat(result[0].getTitle()).isEqualTo(saved[1].getTitle());
    }

    @Test
    public void remove_by_id_works_correctly() throws ItemNotFoundException {
        var items = new Book[]{TestUtils.createBook("title"), TestUtils.createBook("title2")};
        var saved = dbLibrary.addAll(items, Book.class);

        dbLibrary.removeItem(saved[0].getId(), Book.class);

        var result = dbLibrary.getAll(Book.class);
        assertThat(result).hasSize(1);
        assertThat(result[0].getTitle()).isEqualTo(saved[1].getTitle());
    }

}
