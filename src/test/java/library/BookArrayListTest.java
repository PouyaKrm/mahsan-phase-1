package library;

import org.example.library.Book;
import org.example.library.BookArrayList;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class BookArrayListTest {
    @Test
    public void add_book_below_capacity() {
        BookArrayList list = new BookArrayList(2);

        list.addBook(createBook());
        list.addBook(createBook());

        Assert.assertEquals(list.getBooks().length, 2);
    }

    @Test
    public void add_book_above_capacity() {
        BookArrayList list = new BookArrayList(2);
        Book[] books = new Book[] {
                createBook(),
                createBook(),
                createBook(),
        };
        list.addBook(books[0]);
        list.addBook(books[1]);
        list.addBook(books[2]);
        Assert.assertEquals(list.getOriginalBooks().length, 4);
        Assert.assertArrayEquals(list.getOriginalBooks(), new Book[]{books[0], books[1], books[2], null});
        Assert.assertArrayEquals(list.getBooks(), new Book[]{books[0], books[1], books[2]});
    }

    @Test
    public void remove_book() {
        BookArrayList list = new BookArrayList(2);
        Book[] books = new Book[] {
                createBook(),
                createBook(),
        };
        list.addBook(books[0]);
        list.addBook(books[1]);

        list.removeBook(books[1]);

        Assert.assertArrayEquals(new Book[]{books[0], null}, list.getOriginalBooks());
        Assert.assertArrayEquals(list.getBooks(), new Book[]{books[0]});
    }

    @Test
    public void search_by_title_works_correctly() {
        BookArrayList list = new BookArrayList(3);
        Book[] books = new Book[] {
                createBook("t1"),
                createBook("t2"),
                createBook("t3"),
        };
        list.addBook(books[0]);
        list.addBook(books[1]);
        list.addBook(books[2]);

        var b = list.searchByTitle(books[1].getTitle());

        Assert.assertTrue(b.isPresent());
        Assert.assertEquals(b.get(), books[1]);
    }

    @Test
    public void search_by_author_works_correctly() {
        BookArrayList list = new BookArrayList(3);
        Book[] books = new Book[] {
                createBookByAuthro("auth1"),
                createBookByAuthro("auth2"),
                createBookByAuthro("auth3"),
        };
        list.addBook(books[0]);
        list.addBook(books[1]);
        list.addBook(books[2]);

        var b = list.searchByAuthor(books[1].getAuthor());

        Assert.assertTrue(b.isPresent());
        Assert.assertEquals(b.get(), books[1]);

    }

    @Test
    public void add_after_remove_works_correctly() {
        BookArrayList list = new BookArrayList(3);
        Book[] books = new Book[] {
                createBookByAuthro("auth1"),
                createBookByAuthro("auth2"),
                createBookByAuthro("auth3"),
        };
        list.addBook(books[0]);
        list.addBook(books[1]);
        list.addBook(books[2]);

        list.removeBook(books[1]);
        list.addBook(books[1]);

        Assert.assertArrayEquals(list.getBooks(), new Book[]{books[0], books[1], books[2]});

    }

    @Test
    public void sort_works_correctly() {
        BookArrayList list = new BookArrayList(3);
        Book[] books = new Book[] {
                createBookByPubDate(LocalDate.now()),
                createBookByPubDate(LocalDate.now().minusYears(1)),
                createBookByPubDate(LocalDate.now().minusYears(2)),
        };
        list.addBook(books[0]);
        list.addBook(books[1]);
        list.addBook(books[2]);

        list.sortByPubDate();
        Assert.assertEquals(list.getBooks()[0], books[2]);
        Assert.assertEquals(list.getBooks()[1], books[1]);
        Assert.assertEquals(list.getBooks()[2], books[0]);

    }

    public Book createBook() {
        return new Book("book2", "author2", LocalDate.now().minusYears(1), Book.Status.Exist);
    }

    public Book createBook(String title) {
        return new Book(title, "author2", LocalDate.now().minusYears(1), Book.Status.Exist);
    }

    public Book createBookByAuthro(String author) {
        return new Book("title", author, LocalDate.now().minusYears(1), Book.Status.Exist);
    }

    public Book createBookByPubDate(LocalDate localDate) {
        return new Book("title", "author", localDate, Book.Status.Exist);
    }
}
