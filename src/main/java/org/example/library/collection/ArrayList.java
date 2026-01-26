package org.example.library.collection;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

public class ArrayList<T> implements LibraryCollection<T> {


    private Object[] items;
    private final int defualtCapacity = 2;
    private int size = 0;
    private int added = 0;

    public ArrayList() {
        items = new Object[defualtCapacity];
    }

    public ArrayList(int capacity) {
        items = new Object[capacity];
    }

    @Override
    public T[] getItems(Class<T> clazz) {
        var copy = new Object[size];
        var i = 0;
        for (var book : items) {
            if (Objects.isNull(book)) {
                continue;
            }
            copy[i] = book;
            i++;
        }
        return Arrays.copyOf(copy, copy.length,
                (Class<? extends T[]>) java.lang.reflect.Array
                        .newInstance(clazz, 0).getClass());
    }

    public T[] getOriginalBooks(Class<T> clazz) {
        return Arrays.copyOf(items, items.length,
                (Class<? extends T[]>) java.lang.reflect.Array
                        .newInstance(clazz, 0).getClass());
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
        for (var b : bs) {
            if (Objects.isNull(b)) {
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
    public T[] search(Predicate<T> predicate, Class<T> clazz) {
        int count = 0;
        Object[] arr = new Object[items.length];
        for (int i = 0; i < items.length; i++) {
            if(Objects.isNull(items[i])) {
                continue;
            }
            if (predicate.test((T) items[i])) {
                arr[count] = items[i];
                count++;
            }
        }
        var result = new Object[count];
        System.arraycopy(arr, 0, result, 0, count);
        return Arrays.copyOf(result, result.length,
                (Class<? extends T[]>) java.lang.reflect.Array
                        .newInstance(clazz, 0).getClass());
    }

    @Override
    public void sort(Comparator<T> comparator) {
        T[] array = (T[]) items;
        Arrays.sort(array, comparator);
    }

    @Override
    public void addAll(T[] books) {
        for (var book : books) {
            add(book);
        }
    }

    @Override
    public int size() {
        return size;
    }
}
