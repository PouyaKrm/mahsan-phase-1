package library;

import org.example.constansts.ResourceType;
import org.example.constansts.SearchOperation;
import org.example.constansts.SearchField;
import org.example.exception.ItemNotFoundException;
import org.example.library.Library;
import org.example.library.dto.SearchDTO;
import org.example.library.model.book.Book;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import java.util.List;

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
    public void search_by_title_resource_type_works_correctly() {
        var library = new Library();
        var book = TestUtils.createBook();
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        article.setTitle(book.getTitle());
        library.addItem(book);
        library.addItem(magazine);
        library.addItem(article);
        List<SearchDTO> searchDTOS = new java.util.ArrayList<>();
        searchDTOS.add(new SearchDTO(SearchField.RESOURCE_TYPE, ResourceType.BOOK.toString(), SearchOperation.EQ));
        searchDTOS.add(new SearchDTO(SearchField.TITLE, book.getTitle(), SearchOperation.EQ));

        var searched = library.search(searchDTOS);

        Assert.assertEquals(1, searched.length);
        Assert.assertEquals(book, searched[0]);
    }

    @Test
    public void search_by_status_works_correctly() {
        var library = new Library();
        var book = TestUtils.createBook();
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book);
        library.addItem(magazine);
        library.addItem(article);
        List<SearchDTO> searchDTOS = new java.util.ArrayList<>();;
        searchDTOS.add(new SearchDTO(SearchField.Status, book.getStatus().toString(), SearchOperation.EQ));

        var searched = library.search(searchDTOS);

        Assert.assertEquals(1, searched.length);
        Assert.assertEquals(book, searched[0]);

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

    @Test
    public void borrow_book_works_correctly() throws ItemNotFoundException {
        var library = new Library();
        var book = TestUtils.createBook();
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book);
        library.addItem(magazine);
        library.addItem(article);
        library.addItem(TestUtils.createBook("book new"));

        library.borrowItem(book.getTitle());

        var items = library.getBorrowedItems();
        Assert.assertEquals(1, items.length);
        Assert.assertEquals(book, items[0]);
        Assert.assertEquals(book.getStatus(), Book.Status.BORROWED);

    }

    @Test(expected = ItemNotFoundException.class)
    public void borrow_book_throws_ItemNotFoundException() throws ItemNotFoundException {
        var library = new Library();
        var book = TestUtils.createBook();
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book);
        library.addItem(magazine);
        library.addItem(article);

        library.borrowItem("fake");
    }


    @Test
    public void get_borrowed_book_works_correctly() {
        var library = new Library();
        var book = TestUtils.createBook();
        book.setStatus(Book.Status.BORROWED);
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book);
        library.addItem(magazine);
        library.addItem(article);
        library.addItem(TestUtils.createBook("book new"));

        var items = library.getBorrowedItems();

        Assert.assertEquals(1, items.length);
        Assert.assertEquals(book, items[0]);
    }

    @Test
    public void return_book_works_correctly() throws ItemNotFoundException {
        var library = new Library();
        var book = TestUtils.createBook();
        book.setStatus(Book.Status.BORROWED);
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book);
        library.addItem(magazine);
        library.addItem(article);
        library.addItem(TestUtils.createBook("book new"));

        var item = library.returnItem(book.getTitle());

        Assert.assertEquals(book, item);
        Assert.assertEquals(book.getStatus(), Book.Status.EXIST);
    }

    @Test(expected = ItemNotFoundException.class)
    public void return_book_throws_item_not_found_exception() throws ItemNotFoundException {
        var library = new Library();
        var book = TestUtils.createBook();
        book.setStatus(Book.Status.BORROWED);
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book);
        library.addItem(magazine);
        library.addItem(article);

        library.returnItem("fake");
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
