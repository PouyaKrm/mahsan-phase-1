package library;

import org.example.library.Library;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class LibraryTest {

//    @Test
//    public void add_book_works_correctly() {
//        var library = new Library();
//
//        library.addItem(new Book("book1", "author1", LocalDate.now(), Book.Status.EXIST));
//
//        Assert.assertEquals(1, library.getAll().length);
//    }
//
//    @Test
//    public void search_by_title_works_correctly() {
//        var library = new Library();
//        var title = "title";
//        var book = new Book(title, "author1", LocalDate.now(), Book.Status.EXIST);
//        library.addItem(book);
//
//        var searched = library.findByTitle(title);
//
//        Assert.assertTrue(searched.isPresent());
//        Assert.assertEquals(book, searched.get());
//    }
//
//    @Test
//    public void search_by_author_works_correctly() {
//        var library = new Library();
//        var author = "title";
//        var book = new Book("title", author, LocalDate.now(), Book.Status.EXIST);
//        library.addItem(book);
//
//        var searched = library.findByAuthor(author);
//
//        Assert.assertTrue(searched.isPresent());
//        Assert.assertEquals(book, searched.get());
//    }
//
//    @Test
//    public void remove_book_works_correctly() {
//        var library = new Library();
//        var book = new Book("book1", "author1", LocalDate.now(), Book.Status.EXIST);
//        library.addItem(book);
//
//        library.removeItem(book);
//
//        Assert.assertEquals(library.getAll().length, 0);
//    }
//
//    @Test
//    public void sort_works_correctly() {
//
//        var library = new Library();
//        var book1 = new Book("book1", "author1", LocalDate.now(), Book.Status.EXIST);
//        var book2 = new Book("book2", "author2", LocalDate.now().minusYears(1), Book.Status.EXIST);
//        library.addItem(book1);
//        library.addItem(book2);
//
//        library.sortByPublicationDate();
//
//        Assert.assertEquals(library.getAll()[0], book2);
//        Assert.assertEquals(library.getAll()[1], book1);
//    }

}
