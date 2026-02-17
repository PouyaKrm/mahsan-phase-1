package library;

import org.example.exception.BaseException;
import org.example.exception.InvalidOperationException;
import org.example.exception.ItemNotFoundException;
import org.example.library.DbLibraryImpl;
import org.example.library.model.borrow.BorrowModel;
import org.example.library.model.borrow.BorrowRepository;
import org.example.library.model.borrow.BorrowRepositoryImpl;
import org.example.library.model.library.article.Article;
import org.example.library.model.library.article.ArticleRepositoryImpl;
import org.example.library.model.library.book.Book;
import org.example.library.model.library.book.BookRepositoryImpl;
import org.example.library.model.library.magazine.Magazine;
import org.example.library.model.library.magazine.MagazineRepositoryImpl;
import org.example.library.model.user.User;
import org.example.library.model.user.UserRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import utils.TestUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DBLibraryImplTest {
    DbLibraryImpl dbLibrary = new DbLibraryImpl();
    private final MagazineRepositoryImpl magazineRepository = MagazineRepositoryImpl.getInstance();
    private final BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
    private final ArticleRepositoryImpl articleRepository = ArticleRepositoryImpl.getInstance();
    private final BorrowRepository borrowRepository = BorrowRepositoryImpl.getInstance();
    @Before
    public void cleanUp() throws SQLException {
        magazineRepository.removeAll();
        bookRepository.removeAll();
        articleRepository.removeAll();
        borrowRepository.removeAll();
    }


    @Test
    public void getAll_books_works_correctly() {
        var items = new Book[]{TestUtils.createBook("title"), TestUtils.createBook("title2")};
        dbLibrary.addAll(items, Book.class);

        var result = dbLibrary.getAll(Book.class);

        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void getAll_magazines_works_correctly() {
        var items = new Magazine[]{TestUtils.createMagazine("title"), TestUtils.createMagazine("title2")};
        dbLibrary.addAll(items, Magazine.class);

        var result = dbLibrary.getAll(Magazine.class);

        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void getAll_articles_works_correctly() {
        var items = new Article[]{TestUtils.createArticle("title"), TestUtils.createArticle("title2")};
        dbLibrary.addAll(items, Article.class);

        var result = dbLibrary.getAll(Article.class);

        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void add_book_works_correctly() {
        var items = new Book[]{TestUtils.createBook("title"), TestUtils.createBook("title2")};

        Arrays.stream(items).forEach(item -> dbLibrary.addItem(item, Book.class));

        var result = dbLibrary.getAll(Book.class);
        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void add_article_works_correctly() {
        var items = new Article[]{TestUtils.createArticle("title"), TestUtils.createArticle("title2")};

        Arrays.stream(items).forEach(item -> dbLibrary.addItem(item, Article.class));

        var result = dbLibrary.getAll(Article.class);
        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void add_magazine_works_correctly() {
        var items = new Magazine[]{TestUtils.createMagazine("title"), TestUtils.createMagazine("title2")};

        Arrays.stream(items).forEach(item -> dbLibrary.addItem(item, Magazine.class));

        var result = dbLibrary.getAll(Magazine.class);
        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void addAll_books_works_correctly() {
        var items = new Book[]{TestUtils.createBook("title"), TestUtils.createBook("title2")};

        dbLibrary.addAll(items, Book.class);

        var result = dbLibrary.getAll(Book.class);
        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void addAll_articles_works_correctly() {
        var items = new Article[]{TestUtils.createArticle("title"), TestUtils.createArticle("title2")};

        dbLibrary.addAll(items, Article.class);

        var result = dbLibrary.getAll(Article.class);
        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void addAll_magazines_works_correctly() {
        var items = new Magazine[]{TestUtils.createMagazine("title"), TestUtils.createMagazine("title2")};

        dbLibrary.addAll(items, Magazine.class);

        var result = dbLibrary.getAll(Magazine.class);
        assertThat(result).hasSize(items.length);
        assertThat(result[0].getTitle()).isEqualTo(items[0].getTitle());
        assertThat(result[1].getTitle()).isEqualTo(items[1].getTitle());
    }

    @Test
    public void get_item_works_correctly() throws SQLException, ItemNotFoundException {
        var items = new Book[]{TestUtils.createBook("title"), TestUtils.createBook("title2")};
        dbLibrary.addAll(items, Book.class);

        var result = dbLibrary.getItem(items[0].getId(), Book.class);

        assertThat(result.getId()).isEqualTo(items[0].getId());
    }

    @Test(expected = ItemNotFoundException.class)
    public void get_item_throws_ItemNotFoundException() throws ItemNotFoundException {
        var items = new Book[]{TestUtils.createBook("title"), TestUtils.createBook("title2")};
        dbLibrary.addAll(items, Book.class);

        dbLibrary.getItem((new Random()).nextLong(1000, 100000), Book.class);
    }


    @Test
    public void remove_one_book_works_correctly() {
        var items = new Book[]{TestUtils.createBook("title"), TestUtils.createBook("title2")};
        var saved = dbLibrary.addAll(items, Book.class);

        dbLibrary.removeItem(saved[0], Book.class);

        var result = dbLibrary.getAll(Book.class);
        assertThat(result).hasSize(1);
        assertThat(result[0].getTitle()).isEqualTo(saved[1].getTitle());
    }

    @Test
    public void remove_one_article_works_correctly() {
        var items = new Article[]{TestUtils.createArticle("title"), TestUtils.createArticle("title2")};
        var saved = dbLibrary.addAll(items, Article.class);

        dbLibrary.removeItem(saved[0], Article.class);

        var result = dbLibrary.getAll(Article.class);
        assertThat(result).hasSize(1);
        assertThat(result[0].getTitle()).isEqualTo(saved[1].getTitle());
    }

    @Test
    public void remove_one_magazine_works_correctly() {
        var items = new Magazine[]{TestUtils.createMagazine("title"), TestUtils.createMagazine("title2")};
        var saved = dbLibrary.addAll(items, Magazine.class);

        dbLibrary.removeItem(saved[0], Magazine.class);

        var result = dbLibrary.getAll(Magazine.class);
        assertThat(result).hasSize(1);
        assertThat(result[0].getTitle()).isEqualTo(saved[1].getTitle());
    }

    @Test
    public void remove_by_id_works_correctly() throws ItemNotFoundException {
        var items = new Book[]{TestUtils.createBook("title"), TestUtils.createBook("title2")};
        var saved = dbLibrary.addAll(items, Book.class);

        dbLibrary.removeItem(saved[0].getId(), Book.class);

        var result = dbLibrary.getAll(Book.class);
        assertThat(result).hasSize(1);
        assertThat(result[0].getTitle()).isEqualTo(saved[1].getTitle());
    }

    @Test
    public void test_borrow_item() throws SQLException, InvalidOperationException, ItemNotFoundException {
        var item = TestUtils.createBook();
        bookRepository.save(item);
        var user = TestUtils.createUser();
        var userRepo = UserRepositoryImpl.getInstance();
        userRepo.save(user);

        dbLibrary.borrowItem(item.getId());

        var borrowRepository = BorrowRepositoryImpl.getInstance();
        var b = borrowRepository.findByUserIdBookId(user.getId(), item.getId());
        var savedBook = bookRepository.getOne(item.getId());
        assertThat(b.getBookId()).isEqualTo(item.getId());
        assertThat(b.getUserId()).isEqualTo(user.getId());
        assertThat(b.getBorrowedAt()).isNotNull();
        assertThat(b.getReturnedAt()).isNull();
        assertThat(savedBook.getStatus()).isEqualTo(Book.Status.BORROWED);
    }

    @Test(expected = InvalidOperationException.class)
    public void item_already_borrowed() throws InvalidOperationException, ItemNotFoundException, SQLException {
        var item = TestUtils.createBook();
        bookRepository.save(item);
        var user = new User();
        user.setName("username");
        var userRepo = UserRepositoryImpl.getInstance();
        userRepo.save(user);
        var borrow = new BorrowModel();
        borrow.setBookId(item.getId());
        borrow.setUserId(user.getId());
        var borrowRepository = BorrowRepositoryImpl.getInstance();
        borrowRepository.save(borrow);

        dbLibrary.borrowItem(item.getId());
    }

    @Test
    public void return_item_works_correctly() throws SQLException, BaseException {
        var user  = TestUtils.createUser();
        var userRepo = UserRepositoryImpl.getInstance();
        userRepo.save(user);
        var book = TestUtils.createBook();
        book.setStatus(Book.Status.BORROWED);
        BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
        bookRepository.save(book);
        var borrow = new BorrowModel();
        borrow.setUserId(user.getId());
        borrow.setBookId(book.getId());
        borrow.setBorrowedAt(LocalDate.now());
        borrowRepository.save(borrow);

        var b = bookRepository.returnBook(user.getId(), book.getId());

        assertThat(b.getId()).isEqualTo(book.getId());
        assertThat(b.getStatus()).isEqualTo(Book.Status.EXIST);
    }

}
