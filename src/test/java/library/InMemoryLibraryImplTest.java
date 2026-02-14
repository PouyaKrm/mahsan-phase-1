package library;

import org.example.constansts.ResourceType;
import org.example.constansts.SearchOperation;
import org.example.constansts.SearchField;
import org.example.exception.ItemNotFoundException;
import org.example.library.InMemoryLibraryImpl;
import org.example.library.dto.SearchDTO;
import org.example.library.model.article.Article;
import org.example.library.model.book.Book;
import org.example.library.model.magazine.Magazine;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class InMemoryLibraryImplTest {

    @Test
    public void add_book_works_correctly() throws IllegalAccessException, SQLException {
        var library = new InMemoryLibraryImpl();
        library.addItem(TestUtils.createBook(), Book.class);
        library.addItem(TestUtils.createArticle(), Article.class);
        library.addItem(TestUtils.createMagazine(), Magazine.class);

        Assert.assertEquals(1, library.getAllBooks().length);
        Assert.assertEquals(1, library.getAllMagazines().length);
        Assert.assertEquals(1, library.getAllArticles().length);
    }

    @Test
    public void add_works_correctly() {
        var library = new InMemoryLibraryImpl();
        library.addItem(TestUtils.createBook(), Book.class);
        library.addItem(TestUtils.createArticle(), Article.class);
        library.addItem(TestUtils.createMagazine(), Magazine.class);

        Assert.assertEquals(1, library.getAllBooks().length);
        Assert.assertEquals(1, library.getAllArticles().length);
        Assert.assertEquals(1, library.getAllMagazines().length);
    }

    @Test
    public void search_by_title_resource_type_works_correctly() throws SQLException {
        var library = new InMemoryLibraryImpl();
        var book = TestUtils.createBook();
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        article.setTitle(book.getTitle());
        library.addItem(book, Book.class);
        library.addItem(magazine, Magazine.class);
        library.addItem(article, Article.class);
        List<SearchDTO> searchDTOS = new java.util.ArrayList<>();
        searchDTOS.add(new SearchDTO(SearchField.RESOURCE_TYPE, ResourceType.BOOK.toString(), SearchOperation.EQ));
        searchDTOS.add(new SearchDTO(SearchField.TITLE, book.getTitle(), SearchOperation.EQ));

        var searched = library.search(searchDTOS);

        Assert.assertEquals(1, searched.length);
        Assert.assertEquals(book, searched[0]);
    }

    @Test
    public void search_by_status_works_correctly() throws SQLException {
        var library = new InMemoryLibraryImpl();
        var book = TestUtils.createBook();
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book, Book.class);
        library.addItem(magazine, Magazine.class);
        library.addItem(article, Article.class);
        List<SearchDTO> searchDTOS = new java.util.ArrayList<>();;
        searchDTOS.add(new SearchDTO(SearchField.STATUS, book.getStatus().toString(), SearchOperation.EQ));

        var searched = library.search(searchDTOS);

        Assert.assertEquals(1, searched.length);
        Assert.assertEquals(book, searched[0]);

    }



    @Test
    public void remove_book_works_correctly() throws SQLException {
        var library = new InMemoryLibraryImpl();
        var book = TestUtils.createBook();
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book, Book.class);
        library.addItem(magazine, Magazine.class);
        library.addItem(article, Article.class);

        library.removeItem(book, Book.class);

        Assert.assertEquals(library.getAllBooks().length, 0);
        Assert.assertEquals(library.getAllMagazines()[0], magazine);
        Assert.assertEquals(library.getAllArticles()[0], article);
    }

    @Test
    public void remove_works_correctly() throws SQLException {
        var library = new InMemoryLibraryImpl();
        var book = TestUtils.createBook();
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book, Book.class);
        library.addItem(magazine, Magazine.class);
        library.addItem(article, Article.class);

        library.removeItem(book, Book.class);

        Assert.assertEquals(library.getAllBooks().length, 0);
        Assert.assertEquals(1, library.getAllMagazines().length);
        Assert.assertEquals(1, library.getAllArticles().length);
    }

    @Test
    public void borrow_book_works_correctly() throws ItemNotFoundException, SQLException {
        var library = new InMemoryLibraryImpl();
        var book = TestUtils.createBook();
        var random = new Random();
        book.setId(random.nextLong());
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book, Book.class);
        library.addItem(magazine, Magazine.class);
        library.addItem(article, Article.class);
        library.addItem(TestUtils.createBook("book new"), Book.class);

        library.borrowItem(book.getId());

        var items = library.getBorrowedItems();
        Assert.assertEquals(1, items.length);
        Assert.assertEquals(book, items[0]);
        Assert.assertEquals(book.getStatus(), Book.Status.BORROWED);

    }

    @Test(expected = ItemNotFoundException.class)
    public void borrow_book_throws_ItemNotFoundException() throws ItemNotFoundException, SQLException {
        var library = new InMemoryLibraryImpl();
        var book = TestUtils.createBook();
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book, Book.class);
        library.addItem(magazine, Magazine.class);
        library.addItem(article, Article.class);

        library.borrowItem(-1L);
    }


    @Test
    public void get_borrowed_book_works_correctly() throws SQLException {
        var library = new InMemoryLibraryImpl();
        var book = TestUtils.createBook();
        book.setStatus(Book.Status.BORROWED);
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book, Book.class);
        library.addItem(magazine, Magazine.class);
        library.addItem(article, Article.class);
        library.addItem(TestUtils.createBook("book new"), Book.class);

        var items = library.getBorrowedItems();

        Assert.assertEquals(1, items.length);
        Assert.assertEquals(book, items[0]);
    }

    @Test
    public void return_book_works_correctly() throws ItemNotFoundException, SQLException {
        var library = new InMemoryLibraryImpl();
        var book = TestUtils.createBook();
        var random = new Random();
        book.setId(random.nextLong());
        book.setStatus(Book.Status.BORROWED);
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book, Book.class);
        library.addItem(magazine, Magazine.class);
        library.addItem(article, Article.class);
        library.addItem(TestUtils.createBook("book new"), Book.class);

        var item = library.returnItem(book.getId());

        Assert.assertEquals(book, item);
        Assert.assertEquals(book.getStatus(), Book.Status.EXIST);
    }

    @Test(expected = ItemNotFoundException.class)
    public void return_book_throws_item_not_found_exception() throws ItemNotFoundException, SQLException {
        var library = new InMemoryLibraryImpl();
        var book = TestUtils.createBook();
        book.setStatus(Book.Status.BORROWED);
        var magazine = TestUtils.createMagazine();
        var article = TestUtils.createArticle();
        library.addItem(book, Book.class);
        library.addItem(magazine, Magazine.class);
        library.addItem(article, Article.class);

        library.returnItem(-1L);
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
