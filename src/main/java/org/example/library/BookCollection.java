package org.example.library;

import java.util.Optional;

public interface BookCollection {
    Book[] getBooks();
    void addBook(Book book);
    Book removeBook(Book book);
    Optional<Book> searchByTitle(String title);
    Optional<Book> searchByAuthor(String author);
    void sortByPubDate();
}
