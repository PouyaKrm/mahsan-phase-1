package org.example.library;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Library {

    private final List<Book> books = new ArrayList();

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public void printBooks() {
        books.forEach(book -> System.out.println(book));
    }

    public Optional<Book> findByTitle(String title) {
       return books.stream().filter(book -> book.getTitle().contains(title)).findFirst();
    }

    public Optional<Book> findByAuthor(String author) {
        return books.stream().filter(book -> book.getAuthor().contains(author)).findFirst();
    }

    public void sortByPublicationDate() {
        books.sort(Comparator.comparingLong(book -> book.getPubDate().toEpochDay()));
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }
}
