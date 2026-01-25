package library.collection;

import org.example.library.model.Book;
import utils.TestUtils;
import org.example.library.collection.LinkedList;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;

public class LinkedListTest {

    @Test
    public void add_node() {
        LinkedList<Book> list = new LinkedList<Book>();
        var b = TestUtils.createBook();

        list.add(b);

        Assert.assertEquals(list.getSize(), 1);
        Assert.assertEquals(b, list.getItems()[0]);
    }

    @Test
    public void remove_node() {
        LinkedList<Book> list = new LinkedList<Book>();
        var b = TestUtils.createBook();
        var b2 = TestUtils.createBook();
        list.add(b);
        list.add(b2);

        list.remove(b2);

        Assert.assertEquals(1, list.getSize());
        Assert.assertEquals(b, list.getItems()[0]);
    }

    @Test
    public void sort() {
        LinkedList<Book> list = new LinkedList<Book>();
        var b = TestUtils.createBook();
        var b2 = TestUtils.createBook();
        b2.setPubDate(b2.getPubDate().minusYears(1));
        list.add(b);
        list.add(b2);

        list.sort(Comparator.comparingLong(item -> item.getPubDate().toEpochDay()));

        Assert.assertEquals(b2, list.getItems()[0]);
        Assert.assertEquals(b, list.getItems()[1]);
    }

    @Test
    public void search_by_title() {
        LinkedList<Book> list = new LinkedList<Book>();
        var b = TestUtils.createBook();
        var b2 = TestUtils.createBook("new titlw");
        list.add(b);
        list.add(b2);

        var result = list.search(book -> book.getTitle().equals(b.getTitle()));

        Assert.assertEquals(1, result.length);
        Assert.assertEquals(b, result[0]);
    }
}
