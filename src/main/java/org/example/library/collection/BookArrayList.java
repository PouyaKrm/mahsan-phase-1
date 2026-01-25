package org.example.library.collection;

import org.example.library.model.BaseModel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class BookArrayList<T extends BaseModel> implements LibraryCollection<T> {


    private Object[] items;
    private final int defualtCapacity = 2;
    private int size = 0;
    private int added = 0;

    public BookArrayList() {
        items = new Object[defualtCapacity];
    }

    public BookArrayList(int capacity) {
        items = new Object[capacity];
    }

    @Override
    public T[] getItems() {
        var copy = new Object[size];
        var i = 0;
        for(var book : items) {
            if (Objects.isNull(book)) {
                continue;
            }
            copy[i] = book;
            i++;
        }
        return (T[]) copy;
    }

    public T[] getOriginalBooks() {
        return (T[]) items;
    }

    @Override
    public void add(T book) {
        if (added == items.length) {
            items = expandArray(items);
        }
        items[added] = book;
        added++;
        size++;
    }

    private Object[] expandArray(Object[] bs) {
        var newArray = new Object[2 * items.length];
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
    public T remove(T book) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(book)) {
                var t = items[i];
                items[i] = null;
                size--;
                return (T) t;
            }
        }
        return null;
    }

    @Override
    public T[] search(Predicate<T> predicate) {
        int count = 0;
        Object[] arr = new Object[items.length];
        for (int i = 0; i < items.length; i++) {
            if (predicate.test((T) items[i])) {
                arr[count] = items[i];
                count++;
            }
        }
        var result = new Object[count];
        System.arraycopy(arr, 0, result, 0, count);
        return (T[]) result;
    }

    @Override
    public void sort(Comparator<T> comparator) {
        T[] array = (T[]) items;
        Arrays.sort(array, comparator);
    }

    @Override
    public void addAll(T[] books) {
        for(var book : books) {
            add(book);
        }
    }
}
