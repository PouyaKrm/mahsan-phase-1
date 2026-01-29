package org.example.library.collection;

import java.util.Comparator;
import java.util.function.Predicate;

public interface LibraryCollection<T extends Object> {
    T[] getItems(Class<T> clazz);
    void add(T book);
    T remove(T book);
    T remove(Predicate<T> predicate);
    T[] search(Predicate<T> predicate, Class<T> clazz);
    void sort(Comparator<T> comparator);
    void addAll(T[] books);
    int size();
}
