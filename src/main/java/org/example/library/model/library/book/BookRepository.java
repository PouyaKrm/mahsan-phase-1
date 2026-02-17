package org.example.library.model.library.book;

import org.example.exception.BaseException;
import org.example.library.model.library.LibraryModelRepository;
import org.example.library.dto.BorrowAggregate;

import java.sql.SQLException;

public interface BookRepository extends LibraryModelRepository<Book> {

    Book[] getAllByStatus(Book.Status status) throws SQLException;

    Book[] getNonBorrowedBooks() throws SQLException;

    Book returnBook(Long userId, Long bookId) throws SQLException, BaseException;

    BorrowAggregate[] getBorrowedBooksCount(int maxResult) throws SQLException;
}
