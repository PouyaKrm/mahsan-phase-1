package library.model.user;

import org.example.library.model.borrow.BorrowModel;
import org.example.library.model.borrow.BorrowRepository;
import org.example.library.model.borrow.BorrowRepositoryImpl;
import org.example.library.model.library.book.Book;
import org.example.library.model.library.book.BookRepository;
import org.example.library.model.library.book.BookRepositoryImpl;
import org.example.library.model.user.User;
import org.example.library.model.user.UserRepository;
import org.example.library.model.user.UserRepositoryImpl;
import org.junit.Test;
import utils.TestUtils;

import java.sql.SQLException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


public class UserRepositoryImplTest {

    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private final BookRepository bookRepository = BookRepositoryImpl.getInstance();
    private final BorrowRepository borrowRepository = BorrowRepositoryImpl.getInstance();

    @Test
    public void user_created_successfully() throws SQLException {
        var u = new User();
        u.setName("name");
        userRepository.save(u);
        var books = new Book[]{TestUtils.createBook(), TestUtils.createBook()};
        bookRepository.saveAll(books, Book.class);
        var borrows = new BorrowModel[books.length];
        Arrays.stream(books).map(book -> TestUtils.createBorrow(u.getId(), book.getId())).toList().toArray(borrows);
        borrowRepository.saveAll(borrows, BorrowModel.class);

        var user = userRepository.getDefaultUser();

        assertThat(user.getBorrowedBooks().size()).isEqualTo(2);
    }
}
