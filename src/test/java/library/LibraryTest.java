package library;

import org.example.constansts.SearchField;
import org.example.library.Library;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import java.util.Map;

public class LibraryTest {

    @Test
    public void add_book_works_correctly() throws IllegalAccessException {
        var library = new Library();
        library.addItem(TestUtils.createBook());
        library.addItem(TestUtils.createArticle());
        library.addItem(TestUtils.createMagazine());

        Assert.assertEquals(3, library.getAll().length);
    }

    @Test
    public void search_works_correctly() {
        var library = new Library();
        var book = TestUtils.createBook();
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book);
        library.addItem(magazine);
        library.addItem(article);

        var searched = library.search(Map.ofEntries(Map.entry(SearchField.TITLE, article.getTitle())));

        Assert.assertEquals(1, searched.length);
        Assert.assertEquals(article, searched[0]);
    }

    @Test
    public void remove_book_works_correctly() {
        var library = new Library();
        var book = TestUtils.createBook();
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book);
        library.addItem(magazine);
        library.addItem(article);

        library.removeItem(book);

        Assert.assertEquals(library.getAll().length, 2);
        Assert.assertEquals(library.getAll()[0], magazine);
        Assert.assertEquals(library.getAll()[1], article);
    }
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
