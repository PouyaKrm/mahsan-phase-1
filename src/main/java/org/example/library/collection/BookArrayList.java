package org.example.library.collection;

import org.example.library.Book;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class BookArrayList implements LibraryCollection<Book> {


    private Book[] books;
    private final int defualtCapacity = 2;
    private int size = 0;
    private int added = 0;

    public BookArrayList() {
        books = new Book[defualtCapacity];
    }

    public BookArrayList(int capacity) {
        books = new Book[capacity];
    }

    @Override
    public Book[] getBooks() {
        var copy = new Book[size];
        var i = 0;
        for(var book : books) {
            if (Objects.isNull(book)) {
                continue;
            }
            copy[i] = book;
            i++;
        }
        return copy;
    }

    public Book[] getOriginalBooks() {
        return books;
    }

    @Override
    public void addBook(Book book) {
        if (added == books.length) {
            books = expandArray(books);
        }
        books[added] = book;
        added++;
        size++;
    }

    private Book[] expandArray(Book[] bs) {
        var newArray = new Book[2 * books.length];
        var i = 0;
        for(var b : bs) {
            if(Objects.isNull(b)) {
                continue;
            }
            newArray[i] = b;
            i++;
        }
        return newArray;
    }

    @Override
    public Book remove(Book book) {
        for (int i = 0; i < books.length; i++) {
            if (books[i].equals(book)) {
                var t = books[i];
                books[i] = null;
                size--;
                return t;
            }
        }
        return null;
    }

    @Override
    public Optional<Book> search(Predicate<Book> predicate) {
        for (int i = 0; i < books.length; i++) {
            if (predicate.test(books[i])) {
                return Optional.of(books[i]);
            }
        }
        return Optional.empty();
    }

    @Override
    public void sort(Comparator<Book> comparator) {
        Arrays.sort(books, comparator);
    }

    @Override
    public void addAll(Book[] books) {
        for(var book : books) {
            addBook(book);
        }
    }
}
