package org.example.library;

import org.example.library.collection.BookArrayList;
import org.example.library.collection.LibraryCollection;

import java.util.*;

public class Library {

    private final LibraryCollection<Book> bookCollection = new BookArrayList();
    private final List<Book> books = new LinkedList<Book>();

    public void addBook(Book book) {
        bookCollection.addBook(book);
    }

    public void removeBook(Book book) {
        bookCollection.remove(book);
    }

    public void printBooks() {
        books.forEach(book -> System.out.println(book));
    }

    public Optional<Book> findByTitle(String title) {
        return bookCollection.search(book -> book.getTitle().contains(title));
    }

    public Optional<Book> findByAuthor(String author) {
        return bookCollection.search(book -> book.getAuthor().contains(author));
    }

    public void sortByPublicationDate() {
        bookCollection.sort(Comparator.comparingLong(book -> book.getPubDate().toEpochDay()));
    }

    public Book[] getBooks() {
        return bookCollection.getBooks();
    }
}
