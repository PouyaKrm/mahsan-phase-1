package library;

import org.example.library.Book;
import org.example.library.Library;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class LibraryTest {

    @Test
    public void add_book_works_correctly() {
        var library = new Library();

        library.addBook(new Book("book1", "author1", LocalDate.now(), Book.Status.Exist));

        Assert.assertEquals(library.getBooks().size(), 1);
    }

    @Test
    public void search_by_title_works_correctly() {
        var library = new Library();
        var title = "title";
        var book = new Book(title, "author1", LocalDate.now(), Book.Status.Exist);
        library.addBook(book);

        var searched = library.findByTitle(title);

        Assert.assertTrue(searched.isPresent());
        Assert.assertEquals(book, searched.get());
    }

    @Test
    public void search_by_author_works_correctly() {
        var library = new Library();
        var author = "title";
        var book = new Book("title", author, LocalDate.now(), Book.Status.Exist);
        library.addBook(book);

        var searched = library.findByAuthor(author);

        Assert.assertTrue(searched.isPresent());
        Assert.assertEquals(book, searched.get());
    }

    @Test
    public void remove_book_works_correctly() {
        var library = new Library();
        var book = new Book("book1", "author1", LocalDate.now(), Book.Status.Exist);
        library.addBook(book);

        library.removeBook(book);

        Assert.assertEquals(library.getBooks().size(), 0);
    }

    @Test
    public void sort_works_correctly() {

        var library = new Library();
        var book1 = new Book("book1", "author1", LocalDate.now(), Book.Status.Exist);
        var book2 = new Book("book2", "author2", LocalDate.now().minusYears(1), Book.Status.Exist);
        library.addBook(book1);
        library.addBook(book2);

        library.sortByPublicationDate();

        Assert.assertEquals(library.getBooks().get(0), book2);
        Assert.assertEquals(library.getBooks().get(1), book1);
    }

}
