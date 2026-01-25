package org.example.library.collection;

import org.example.library.Book;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class LinkedList<T extends Book> implements LibraryCollection<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size = 0;

    @Override
    public T[] getItems() {
        return null;
    }

    @Override
    public void add(T book) {
        var node = new Node<>(book);
        if (head == null) {
            head = node;
            tail = head;
        } else {
            tail.setNext(node);
            tail = node;
        }
        size++;
    }

    public T remove(T data) {
        var nx = head;
        var pre = nx;
        while (nx != null) {
            if (nx.getData().equals(data)) {
                pre.setNext(nx.getNext());
                size--;
                return nx.getData();
            }
            pre = nx;
            nx = nx.getNext();
        }
        return null;
    }

    public T[] search(Predicate<T> predicate) {
        var nx = head;
        int count = 0;
        Object[] arr = new Object[size];
        while (nx != null) {
            if (predicate.test(nx.getData())) {
                arr[count] = nx.getData();
                count++;
            }
            nx = nx.getNext();
        }
        var result = new Object[count];
        System.arraycopy(arr, 0, result, 0, count);
        return (T[]) result;
    }

    public void sort(Comparator<T> comparator) {
        head = mergeSort(head, comparator);
        updateTail();
    }

    @Override
    public void addAll(T[] books) {
        for (var book : books) {
            add(book);
        }
    }

    public Object[] getAll() {
        var ts = new Object[size];
        var nx = head;
        int i = 0;
        while (!Objects.isNull(nx)) {
            ts[i++] = nx.getData();
            nx = nx.getNext();
        }
        return ts;
    }

    public int getSize() {
        return size;
    }

    // Merge Sort
    private Node<T> mergeSort(Node<T> node, Comparator<T> comparator) {
        if (node == null || node.getNext() == null)
            return node;

        Node<T> middle = getMiddle(node);
        Node<T> nextOfMiddle = middle.getNext();
        middle.setNext(null);

        Node<T> left = mergeSort(node, comparator);
        Node<T> right = mergeSort(nextOfMiddle, comparator);

        return sortedMerge(left, right, comparator);
    }


    private Node<T> sortedMerge(Node<T> a, Node<T> b, Comparator<T> comparator) {
        if (a == null) return b;
        if (b == null) return a;
        var dummy = new Node<T>(null);
        Node<T> t = dummy;
        while (a != null && b != null) {
            if (comparator.compare((T) a.getData(), (T) b.getData()) <= 0) {
                t.setNext(a);
                a = a.getNext();
            } else {
                t.setNext(b);
                b = b.getNext();
            }
        }
        tail.setNext(a != null ? a : b);
        return dummy.getNext();
    }

    private Node<T> getMiddle(Node<T> node) {
        if (node == null)
            return node;

        Node<T> slow = node;
        Node<T> fast = node.getNext();

        while (fast != null && fast.getNext() != null) {

            slow = slow.getNext();
            fast = fast.getNext().getNext();
        }

        return slow;
    }

    private void updateTail() {
        if (head == null) {
            tail = null;
            return;
        }

        Node<T> curr = head;
        while (curr.getNext() != null) {
            curr = curr.getNext();
        }
        tail = curr;
    }
}
