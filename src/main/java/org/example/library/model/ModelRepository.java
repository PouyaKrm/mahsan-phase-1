package org.example.library.model;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.book.Book;

import java.sql.SQLException;

public interface ModelRepository<T extends BaseModel> {
    T[] getAll() throws SQLException;
    T save(T model) throws SQLException;
    boolean removeOne(T model) throws SQLException;
    boolean removeOne(Long id) throws SQLException;
    Book getOne(Long id) throws SQLException, ItemNotFoundException;
}
