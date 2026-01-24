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


    private int added = 0;
    private int firstEmptySlut = 0;

    public BookArrayList() {
        books = new Book[defualtCapacity];
    }

    public BookArrayList(int capacity) {
        books = new Book[capacity];
    }

    @Override
    public Book[] getBooks() {
        var copy = new Book[added];
        System.arraycopy(books, 0, copy, 0, added);
        return copy;
    }

    public Book[] getOriginalBooks() {
        return books;
    }

    @Override
    public void addBook(Book book) {
        if (Objects.isNull(books[firstEmptySlut])) {
            books[firstEmptySlut] = book;
        } else if (added == books.length) {
            var newArray = new Book[2 * books.length];
            System.arraycopy(books, 0, newArray, 0, added);
            books = newArray;
            books[added] = book;
        } else {
            var i = findNewEmptySlut();
            books[i] = book;
            firstEmptySlut = i;
        }

        added++;
    }

    private int findNewEmptySlut() {
        for (int i = firstEmptySlut; i < books.length; i++) {
            if (books[i] == null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Book remove(Book book) {
        for (int i = 0; i < books.length; i++) {
            if (books[i].equals(book)) {
                var t = books[i];
                books[i] = null;
                firstEmptySlut = i;
                added--;
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
}
