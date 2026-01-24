package library.collection;

import library.TestUtils;
import org.example.library.Book;
import org.example.library.collection.LinkedList;
import org.example.library.collection.Node;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Comparator;

public class LinkedListTest {

    @Test
    public void add_node() {
        LinkedList<Book> list = new LinkedList<Book>();
        var b = TestUtils.createBook();

        list.addBook(b);

        Assert.assertEquals(list.getSize(), 1);
        Assert.assertEquals(b, list.getAll()[0]);
    }

    @Test
    public void remove_node() {
        LinkedList<Book> list = new LinkedList<Book>();
        var b = TestUtils.createBook();
        var b2 = TestUtils.createBook();
        list.addBook(b);
        list.addBook(b2);

        list.remove(b2);

        Assert.assertEquals(1, list.getSize());
        Assert.assertEquals(b, list.getAll()[0]);
    }

    @Test
    public void sort() {
        LinkedList<Book> list = new LinkedList<Book>();
        var b = TestUtils.createBook();
        var b2 = TestUtils.createBookByPubDate(LocalDate.now().minusYears(1));
        list.addBook(b);
        list.addBook(b2);

        list.sort(Comparator.comparingLong(item -> item.getPubDate().toEpochDay()));

        Assert.assertEquals(b2, list.getAll()[0]);
        Assert.assertEquals(b, list.getAll()[1]);
    }

    @Test
    public void search_by_title() {
        LinkedList<Book> list = new LinkedList<Book>();
        var b = TestUtils.createBook();
        var b2 = TestUtils.createBook("new titlw");
        list.addBook(b);
        list.addBook(b2);

        var result = list.search(book -> book.getTitle().equals(b.getTitle()));

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(b, result.get());
    }
}
