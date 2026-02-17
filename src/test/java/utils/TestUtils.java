package utils;

import org.example.library.model.borrow.BorrowModel;
import org.example.library.model.library.book.Book;
import org.example.library.model.library.article.Article;
import org.example.library.model.library.magazine.Magazine;
import org.example.library.model.user.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Random;

public class TestUtils {
    public static Book createBook() {
        return new Book(LocalDate.now().minusYears(1), "book title", "author2", "content", Book.Status.EXIST);
    }

    public static Book createBookWithId() {
        var b = new Book(LocalDate.now().minusYears(1), "book title", "author2", "content", Book.Status.EXIST);
        var random = new Random();
        b.setId(random.nextLong(1, 1000000000));
        return b;
    }

    public static Book createBook(String title) {
        return new Book(LocalDate.now().minusYears(1), title, "author2", "content", Book.Status.EXIST);
    }

    public static Book createBookByAuthro(String author) {
        return new Book(LocalDate.now().minusYears(1), "title", author, "content", Book.Status.EXIST);
    }

    public static Book createBookByPubDate(LocalDate localDate) {
        return new Book(localDate, "book title", "author", "content", Book.Status.EXIST);
    }

    public static Article createArticle() {
        return new Article(LocalDate.now(), "article title", "author", "conten");
    }

    public static Article createArticleWithId() {
        var a = new Article(LocalDate.now(), "article title", "author", "conten");
        var random = new Random();
        a.setId(random.nextLong(1, 1000000000));
        return a;
    }


    public static Article createArticle(String title) {
        return new Article(LocalDate.now(), title, "author", "conten");
    }

    public static Magazine createMagazine() {
        return new Magazine(LocalDate.now(), "magazine title", "author", "conten");
    }


    public static Magazine createMagazineWithId() {
        var m = new Magazine(LocalDate.now(), "magazine title", "author", "conten");
        var random = new Random();
        m.setId(random.nextLong(1, 1000000000));
        return m;
    }



    public static Magazine createMagazine(String title) {
        return new Magazine(LocalDate.now(), title, "author", "conten");
    }

    public static BorrowModel createBorrow(Long userId, Long bookId) {
        var b = new BorrowModel();
        b.setUserId(userId);
        b.setBookId(bookId);
        b.setBorrowedAt(LocalDate.now());
        return b;
    }

    public static BorrowModel createBorrow(Long userId, Long bookId, LocalDate returnedAt) {
        var b = new BorrowModel();
        b.setUserId(userId);
        b.setBookId(bookId);
        b.setBorrowedAt(LocalDate.now());
        b.setReturnedAt(returnedAt);
        return b;
    }

    public static Connection getTestDBConnection() throws SQLException {
        var random = new Random();
        var url = MessageFormat.format("jdbc:h2:mem:testdb_{0};MODE=MYSQL", random.nextInt(100, 1000));
        var username = "sa";
        return DriverManager.getConnection(url, username, "");
    }


    public static User createUser() {
        var u = new User();
        u.setName("name");
        return u;
    }
}
