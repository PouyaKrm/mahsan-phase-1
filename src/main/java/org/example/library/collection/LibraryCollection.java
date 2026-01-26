package org.example.library.collection;

import org.example.library.model.BaseModel;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public interface LibraryCollection<T extends Object> {
    T[] getItems(Class<T> clazz);
    void add(T book);
    T remove(T book);
    T[] search(Predicate<T> predicate, Class<T> clazz);
    void sort(Comparator<T> comparator);
    void addAll(T[] books);
}
