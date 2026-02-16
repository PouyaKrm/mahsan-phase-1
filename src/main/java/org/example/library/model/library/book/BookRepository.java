package org.example.library.model.library.book;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.library.LibraryModelRepository;

import java.sql.SQLException;
import java.util.List;

public interface BookRepository extends LibraryModelRepository<Book> {

    Book[] getAllByStatus(Book.Status status) throws SQLException;
}
