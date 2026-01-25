package library;

import org.example.library.Book;

import java.time.LocalDate;

public class TestUtils {
    public static Book createBook() {
        return new Book("book2", "author2", LocalDate.now().minusYears(1), Book.Status.EXIST);
    }

    public static Book createBook(String title) {
        return new Book(title, "author2", LocalDate.now().minusYears(1), Book.Status.EXIST);
    }

    public static Book createBookByAuthro(String author) {
        return new Book("title", author, LocalDate.now().minusYears(1), Book.Status.EXIST);
    }

    public static Book createBookByPubDate(LocalDate localDate) {
        return new Book("title", "author", localDate, Book.Status.EXIST);
    }
}
