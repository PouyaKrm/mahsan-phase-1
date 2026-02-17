package org.example.library.model.library.book.dto;

import org.example.library.Displayable;
import org.example.library.model.library.book.Book;

public record BorrowAggregate(Long count, Book book) implements Displayable {
    @Override
    public void display() {
        var msg = new StringBuilder().append("count: ").append(count).append(" - ").append("book: ").append(book);
        System.out.println(msg);
    }
}
