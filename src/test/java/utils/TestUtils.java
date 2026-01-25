package utils;

import org.example.library.Book;
import org.example.library.model.Article;
import org.example.library.model.Magazine;
import org.example.library.model.Reference;

import java.time.LocalDate;

public class TestUtils {
    public static Book createBook() {
        return new Book(LocalDate.now().minusYears(1), "book2", "author2", Book.Status.EXIST);
    }

    public static Book createBook(String title) {
        return new Book(LocalDate.now().minusYears(1), title, "author2", Book.Status.EXIST);
    }

    public static Book createBookByAuthro(String author) {
        return new Book(LocalDate.now().minusYears(1),"title", author, Book.Status.EXIST);
    }

    public static Book createBookByPubDate(LocalDate localDate) {
        return new Book(localDate, "title", "author", Book.Status.EXIST);
    }

    public static Article createArticle() {
        return new Article(LocalDate.now(), "article title", "author", "conten");
    }

    public static Magazine createMagazine() {
        return new Magazine(LocalDate.now(), "title", "author", "conten");
    }

    public static Reference createReference() {
        return new Reference(LocalDate.now(), "title", "author", "conten");
    }
}
