package org.example.library.model.library.book.dto;

import org.example.library.model.library.book.Book;

public record BorrowAggregate(Long count, Book book) {
}
