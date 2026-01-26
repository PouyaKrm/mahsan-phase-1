package utils;

import org.example.library.model.book.Book;
import org.example.library.model.article.Article;
import org.example.library.model.magazine.Magazine;

import java.time.LocalDate;

public class TestUtils {
    public static Book createBook() {
        return new Book(LocalDate.now().minusYears(1), "book title", "author2", "content", Book.Status.EXIST);
    }

    public static Book createBook(String title) {
        return new Book(LocalDate.now().minusYears(1), title, "author2", "content", Book.Status.EXIST);
    }

    public static Book createBookByAuthro(String author) {
        return new Book(LocalDate.now().minusYears(1),"title", author, "content", Book.Status.EXIST);
    }

    public static Book createBookByPubDate(LocalDate localDate) {
        return new Book(localDate, "book title", "author", "content", Book.Status.EXIST);
    }

    public static Article createArticle() {
        return new Article(LocalDate.now(), "article title", "author", "conten");
    }

    public static Magazine createMagazine() {
        return new Magazine(LocalDate.now(), "magazine title", "author", "conten");
    }

}
