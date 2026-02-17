package org.example.library.model.library.book;

import org.example.exception.BaseException;
import org.example.exception.ItemNotFoundException;
import org.example.library.model.library.LibraryModelRepository;
import org.example.library.model.library.book.dto.BorrowAggregate;

import java.sql.SQLException;
import java.util.List;

public interface BookRepository extends LibraryModelRepository<Book> {

    Book[] getAllByStatus(Book.Status status) throws SQLException;

    Book[] getNonBorrowedBooksAtAll() throws SQLException;

    Book returnBook(Long userId, Long bookId) throws SQLException, BaseException;

    BorrowAggregate[] getBorrowedBooksCount(int maxResult) throws SQLException;
}
