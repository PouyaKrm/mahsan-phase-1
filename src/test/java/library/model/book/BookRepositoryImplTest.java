package library.model.book;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.book.BookRepositoryImpl;
import org.example.sql.JdbcConnection;
import org.junit.Before;
import org.junit.Test;
import utils.TestUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

public class BookRepositoryImplTest {

    private Connection connection = JdbcConnection.getConnection();

    @Before
    public void resetDb() throws Exception {
        try (Statement st = connection.createStatement()) {
            st.execute("DROP TABLE IF EXISTS book");

            st.execute("""
                            
                    CREATE TABLE book (
                                     id SERIAL PRIMARY KEY,          -- auto-incrementing ID
                                     title VARCHAR(255) NOT NULL,    -- book title
                                     author VARCHAR(255) NOT NULL,   -- author name
                                     content TEXT,                   -- book content
                                     status VARCHAR(50),             -- e.g., "available", "borrowed"
                                     pub_date BIGINT,                -- publication timestamp (epoch millis)
                                     borrow_date BIGINT              -- borrow timestamp (epoch millis)
                                 );
                            """);
        }
    }

    @Test
    public void create_book_works_correctly() throws SQLException, ItemNotFoundException {
        BookRepositoryImpl bookRepository = new BookRepositoryImpl();
        var book = TestUtils.createBook();

        bookRepository.save(book);

        var found = bookRepository.getOne(book.getId());

        assertThat(book.getTitle()).isEqualTo(found.getTitle());
        assertThat(book.getAuthor()).isEqualTo(found.getAuthor());
        assertThat(book.getContent()).isEqualTo(found.getContent());

    }

    @Test
    public void get_one_works_correctly() throws SQLException, ItemNotFoundException {
        BookRepositoryImpl bookRepository = new BookRepositoryImpl();
        var book = TestUtils.createBook();
        bookRepository.save(book);

        var found = bookRepository.getOne(book.getId());

        assertThat(found.getTitle()).isEqualTo(book.getTitle());
        assertThat(found.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(found.getContent()).isEqualTo(book.getContent());
    }

    @Test
    public void get_all_works_correctly() throws SQLException, ItemNotFoundException {
        BookRepositoryImpl bookRepository = new BookRepositoryImpl();
        var book = TestUtils.createBook();
        var book2 = TestUtils.createBook();
        bookRepository.save(book);
        bookRepository.save(book2);

        var found = bookRepository.getAll();

        assertThat(found.length).isEqualTo(2);

    }

    @Test
    public void remove_one_works_correctly() throws SQLException, ItemNotFoundException {
        BookRepositoryImpl bookRepository = new BookRepositoryImpl();
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
        BookRepositoryImpl bookRepository = new BookRepositoryImpl();
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

}
