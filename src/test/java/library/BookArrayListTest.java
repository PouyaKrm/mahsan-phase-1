package library;

import org.example.library.collection.BookArrayList;
import org.example.library.model.book.Book;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import java.time.LocalDate;
import java.util.Comparator;

public class BookArrayListTest {
    @Test
    public void add_book_below_capacity() {
        BookArrayList<Book> list = new BookArrayList<Book>(2);

        list.add(TestUtils.createBook());
        list.add(TestUtils.createBook());

        Assert.assertEquals(list.getItems(Book.class).length, 2);
    }

    @Test
    public void add_book_above_capacity() {
        BookArrayList<Book> list = new BookArrayList(2);
        Book[] books = new Book[]{
                TestUtils.createBook(),
                TestUtils.createBook(),
                TestUtils.createBook(),
        };
        list.add(books[0]);
        list.add(books[1]);
        list.add(books[2]);
        Assert.assertEquals(list.getOriginalBooks(Book.class).length, 4);
        Assert.assertArrayEquals(list.getOriginalBooks(Book.class), new Book[]{books[0], books[1], books[2], null});
        Assert.assertArrayEquals(list.getItems(Book.class), new Book[]{books[0], books[1], books[2]});
    }

    @Test
    public void remove_book() {
        BookArrayList<Book> list = new BookArrayList(2);
        Book[] books = new Book[]{
                TestUtils.createBook(),
                TestUtils.createBook(),
        };
        list.add(books[0]);
        list.add(books[1]);

        list.remove(books[1]);

        Assert.assertArrayEquals(new Book[]{books[0], null}, list.getOriginalBooks(Book.class));
        Assert.assertArrayEquals(list.getItems(Book.class), new Book[]{books[0]});
    }

    @Test
    public void search_by_title_works_correctly() {
        BookArrayList<Book> list = new BookArrayList<Book>(3);
        Book[] books = new Book[]{
                TestUtils.createBook("t1"),
                TestUtils.createBook("t2"),
                TestUtils.createBook("t3"),
        };
        list.add(books[0]);
        list.add(books[1]);
        list.add(books[2]);

        var b = list.search(book -> book.getTitle().equals(books[1].getTitle()), Book.class);

        Assert.assertEquals(1, b.length);
        Assert.assertEquals(b[0], books[1]);
    }

    @Test
    public void search_by_author_works_correctly() {
        BookArrayList<Book> list = new BookArrayList(3);
        Book[] books = new Book[]{
                TestUtils.createBookByAuthro("auth1"),
                TestUtils.createBookByAuthro("auth2"),
                TestUtils.createBookByAuthro("auth3"),
        };
        list.add(books[0]);
        list.add(books[1]);
        list.add(books[2]);

        var b = list.search((Book book) -> book.getAuthor().equals(books[1].getAuthor()), Book.class);

        Assert.assertEquals(1, b.length);
        Assert.assertEquals(b[0], books[1]);

    }

    @Test
    public void add_after_remove_works_correctly() {
        BookArrayList<Book> list = new BookArrayList(3);
        Book[] books = new Book[]{
                TestUtils.createBookByAuthro("auth1"),
                TestUtils.createBookByAuthro("auth2"),
                TestUtils.createBookByAuthro("auth3"),
        };
        list.add(books[0]);
        list.add(books[1]);
        list.add(books[2]);

        list.remove(books[1]);
        list.add(books[1]);
        Assert.assertArrayEquals(new Book[]{books[0], books[2], books[1]}, list.getItems(Book.class));

    }

    @Test
    public void sort_works_correctly() {
        BookArrayList<Book> list = new BookArrayList(3);
        Book[] books = new Book[]{
                TestUtils.createBookByPubDate(LocalDate.now()),
                TestUtils.createBookByPubDate(LocalDate.now().minusYears(1)),
                TestUtils.createBookByPubDate(LocalDate.now().minusYears(2)),
        };
        list.add(books[0]);
        list.add(books[1]);
        list.add(books[2]);

        list.sort(Comparator.comparingLong(book -> book.getPubDate().compareTo(LocalDate.now())));
        Assert.assertEquals(list.getItems(Book.class)[0], books[2]);
        Assert.assertEquals(list.getItems(Book.class)[1], books[1]);
        Assert.assertEquals(list.getItems(Book.class)[2], books[0]);

    }


}
