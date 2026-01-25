package org.example.library.collection;

import org.example.library.model.BaseModel;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public interface LibraryCollection<T extends BaseModel> {
    T[] getItems();
    void add(T book);
    T remove(T book);
    T[] search(Predicate<T> predicate);
    void sort(Comparator<T> comparator);
    void addAll(T[] books);
}
