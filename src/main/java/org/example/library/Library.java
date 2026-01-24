package org.example.library;

import java.util.*;

public class Library {

    private final BookCollection bookCollection = new BookArrayList();
    private final List<Book> books = new LinkedList<Book>();

    public void addBook(Book book) {
        bookCollection.addBook(book);
    }

    public void removeBook(Book book) {
        bookCollection.removeBook(book);
    }

    public void printBooks() {
        books.forEach(book -> System.out.println(book));
    }

    public Optional<Book> findByTitle(String title) {
        return bookCollection.searchByTitle(title);
    }

    public Optional<Book> findByAuthor(String author) {
        return bookCollection.searchByAuthor(author);
    }

    public void sortByPublicationDate() {
        bookCollection.sortByPubDate();
    }

    public Book[] getBooks() {

        return bookCollection.getBooks();
    }
}
