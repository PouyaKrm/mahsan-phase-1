package org.example.library.collection;

import org.example.library.Book;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public interface LibraryCollection<T extends Book> {
    T[] getBooks();
    void addBook(T book);
    Book remove(T book);
    Optional<T> search(Predicate<T> predicate);
    void sort(Comparator<T> comparator);
    void addAll(T[] books);
}
