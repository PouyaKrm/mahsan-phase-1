package library.model.book;

import org.example.exception.BaseException;
import org.example.exception.InvalidOperationException;
import org.example.exception.ItemNotFoundException;
import org.example.library.model.borrow.BorrowModel;
import org.example.library.model.borrow.BorrowRepository;
import org.example.library.model.borrow.BorrowRepositoryImpl;
import org.example.library.model.library.book.Book;
import org.example.library.model.library.book.BookRepositoryImpl;
import org.example.library.model.user.UserRepository;
import org.example.library.model.user.UserRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import utils.TestUtils;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class BookRepositoryImplTest {

    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private final BorrowRepository borrowRepository = BorrowRepositoryImpl.getInstance();

    @Before
    public void resetDb() throws Exception {
        var bookRepository = BookRepositoryImpl.getInstance();
        bookRepository.removeAll();
        userRepository.removeAll();
    }

    @Test
    public void create_book_works_correctly() throws SQLException, ItemNotFoundException {
        BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
        var book = TestUtils.createBook();

        bookRepository.save(book);

        var found = bookRepository.getOne(book.getId());

        assertThat(book.getTitle()).isEqualTo(found.getTitle());
        assertThat(book.getAuthor()).isEqualTo(found.getAuthor());
        assertThat(book.getContent()).isEqualTo(found.getContent());

    }

    @Test
    public void saveAll_works_correctly() throws SQLException {
        BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
        var book1 = TestUtils.createBook("title1");
        var book2 = TestUtils.createBook("title2");

        var result = bookRepository.saveAll(new Book[]{book1, book2}, Book.class);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(book1, book2);
        assertThat(result[0].getTitle()).isEqualTo(book1.getTitle());
        assertThat(result[1].getTitle()).isEqualTo(book2.getTitle());
    }

    @Test
    public void saveAll_updates() throws SQLException {
        BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
        var book1 = TestUtils.createBook("title1");
        var book2 = TestUtils.createBook("title2");
        bookRepository.saveAll(new Book[]{book1, book2}, Book.class);
        book1.setTitle("updated title 1");
        book2.setTitle("updated title 2");

        bookRepository.saveAll(new Book[]{book1, book2}, Book.class);

        var result = bookRepository.getAll();
        assertThat(result).hasSize(2);
        assertThat(result[0].getTitle()).isEqualTo(book1.getTitle());
        assertThat(result[1].getTitle()).isEqualTo(book2.getTitle());
    }

    @Test
    public void saveAll_saves_new_and_updates_existing() throws SQLException {
        BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
        var book1 = TestUtils.createBook("title1");
        var book2 = TestUtils.createBook("title2");
        bookRepository.save(book2);
        book2.setTitle("updated title 2");

        bookRepository.saveAll(new Book[]{book1, book2}, Book.class);

        var result = bookRepository.getAll();
        assertThat(result).hasSize(2);
        assertThat(result).anySatisfy(item -> {
            assertThat(item.getTitle()).isEqualTo(book2.getTitle());
            assertThat(item.getId()).isEqualTo(book2.getId());
        });
        assertThat(result).anySatisfy(item -> assertThat(item.getTitle()).isEqualTo(book1.getTitle()));
    }

    @Test
    public void get_one_works_correctly() throws SQLException, ItemNotFoundException {
        BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
        var book = TestUtils.createBook();
        bookRepository.save(book);

        var found = bookRepository.getOne(book.getId());

        assertThat(found.getTitle()).isEqualTo(book.getTitle());
        assertThat(found.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(found.getContent()).isEqualTo(book.getContent());
    }

    @Test
    public void get_all_works_correctly() throws SQLException, ItemNotFoundException {
        BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
        var book = TestUtils.createBook();
        var book2 = TestUtils.createBook();
        bookRepository.save(book);
        bookRepository.save(book2);

        var found = bookRepository.getAll();

        assertThat(found.length).isEqualTo(2);

    }

    @Test
    public void remove_one_works_correctly() throws SQLException, ItemNotFoundException {
        BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
        var book = TestUtils.createBook();
        var book2 = TestUtils.createBook();
        bookRepository.save(book);
        bookRepository.save(book2);

        var result = bookRepository.removeOne(book);

        assertThat(result).isTrue();
        var found = bookRepository.getAll();
        assertThat(found.length).isEqualTo(1);
        assertThat(found[0].getTitle()).isEqualTo(book.getTitle());
        assertThat(found[0].getAuthor()).isEqualTo(book.getAuthor());
        assertThat(found[0].getContent()).isEqualTo(book.getContent());
    }

    @Test
    public void remove_one_by_id_works_correctly() throws SQLException, ItemNotFoundException {
        BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
        var book = TestUtils.createBook();
        var book2 = TestUtils.createBook();
        bookRepository.save(book);
        bookRepository.save(book2);

        var result = bookRepository.removeOne(book.getId());

        assertThat(result).isTrue();
        var found = bookRepository.getAll();
        assertThat(found.length).isEqualTo(1);
        assertThat(found[0].getTitle()).isEqualTo(book.getTitle());
        assertThat(found[0].getAuthor()).isEqualTo(book.getAuthor());
        assertThat(found[0].getContent()).isEqualTo(book.getContent());
    }


    @Test
    public void getNonBorrowedBooks_works_correctly() throws SQLException {
        BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
        var books = new Book[]{TestUtils.createBook(), TestUtils.createBook(), TestUtils.createBook()};
        bookRepository.saveAll(books, Book.class);
        var user = TestUtils.createUser();
        userRepository.save(user);
        var borrows = new BorrowModel[]{
                TestUtils.createBorrow(user.getId(), books[0].getId()),
                TestUtils.createBorrow(user.getId(), books[1].getId())
        };
        borrowRepository.saveAll(borrows, BorrowModel.class);

        var foundBooks = bookRepository.getNonBorrowedBooks();
        assertThat(foundBooks.length).isEqualTo(1);
        assertThat(foundBooks[0].getId()).isEqualTo(books[2].getId());
    }

    @Test
    public void getBorrowedBooksCount_works_correctly() throws SQLException {
        BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
        var books = new Book[]{TestUtils.createBook(), TestUtils.createBook(), TestUtils.createBook()};
        bookRepository.saveAll(books, Book.class);
        var user = TestUtils.createUser();
        userRepository.save(user);
        var borrows = new BorrowModel[]{
                TestUtils.createBorrow(user.getId(), books[0].getId(), LocalDate.now()),
                TestUtils.createBorrow(user.getId(), books[0].getId()),
                TestUtils.createBorrow(user.getId(), books[1].getId())
        };
        borrowRepository.saveAll(borrows, BorrowModel.class);

        var result = bookRepository.getBorrowedBooksCount(3);

        assertThat(result.length).isEqualTo(2);
        assertThat(result[0].count()).isEqualTo(2);
        assertThat(result[0].book().getId()).isEqualTo(books[0].getId());
        assertThat(result[1].count()).isEqualTo(1);
        assertThat(result[1].book().getId()).isEqualTo(books[1].getId());
    }

    @Test
    public void return_book_works_correctly() throws SQLException, BaseException {
        var user  = TestUtils.createUser();
        userRepository.save(user);
        var book = TestUtils.createBook();
        BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
        bookRepository.save(book);
        var borrow = new BorrowModel();
        borrow.setUserId(user.getId());
        borrow.setBookId(book.getId());
        borrow.setBorrowedAt(LocalDate.now());
        borrowRepository.save(borrow);

        var b = bookRepository.returnBook(user.getId(), book.getId());
        var foundBorrow = borrowRepository.getOne(borrow.getId());
        assertThat(b.getId()).isEqualTo(book.getId());
        assertThat(b.getStatus()).isEqualTo(Book.Status.EXIST);
        assertThat(foundBorrow.getId()).isEqualTo(borrow.getId());
        assertThat(foundBorrow.getReturnedAt()).isNotNull();
    }

    @Test(expected = ItemNotFoundException.class)
    public void borrow_record_not_found() throws SQLException, BaseException {
        var user  = TestUtils.createUser();
        userRepository.save(user);
        var book = TestUtils.createBook();
        BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
        bookRepository.save(book);

        bookRepository.returnBook(user.getId(), book.getId());
    }

    @Test(expected = InvalidOperationException.class)
    public void return_book_book_already_returned() throws SQLException, BaseException {
        var user  = TestUtils.createUser();
        userRepository.save(user);
        var book = TestUtils.createBook();
        BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
        bookRepository.save(book);
        var borrow = new BorrowModel();
        borrow.setUserId(user.getId());
        borrow.setBookId(book.getId());
        borrow.setBorrowedAt(LocalDate.now());
        borrow.setReturnedAt(LocalDate.now());
        borrowRepository.save(borrow);

        bookRepository.returnBook(user.getId(), book.getId());
    }

    @Test
    public void test_getNonBorrowedBooks_works_correctly() throws SQLException {
        BookRepositoryImpl bookRepository = BookRepositoryImpl.getInstance();
        var books = new Book[]{TestUtils.createBook(), TestUtils.createBook(), TestUtils.createBook()};
        bookRepository.saveAll(books, Book.class);
        var user = TestUtils.createUser();
        userRepository.save(user);
        var borrows = new BorrowModel[]{TestUtils.createBorrow(user.getId(), books[0].getId()), TestUtils.createBorrow(user.getId(), books[1].getId())};
        borrowRepository.saveAll(borrows, BorrowModel.class);

        var foundBooks = bookRepository.getNonBorrowedBooks();

        assertThat(foundBooks.length).isEqualTo(1);
        assertThat(foundBooks[0].getId()).isEqualTo(books[2].getId());
    }

}
