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
    public T[] getBooks() {
        return null;
    }

    @Override
    public void addBook(T book) {
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

    public Optional<T> search(Predicate<T> predicate) {
        var nx = head;
        while (nx != null) {
            if (predicate.test(nx.getData())) {
                return Optional.of(nx.getData());
            }
            nx = nx.getNext();
        }
        return Optional.empty();
    }

    public void sort(Comparator<T> comparator) {
        head = mergeSort(head, comparator);
        updateTail();
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
    private Node mergeSort(Node node, Comparator<T> comparator) {
        if (node == null || node.getNext() == null)
            return node;

        Node middle = getMiddle(node);
        Node nextOfMiddle = middle.getNext();
        middle.setNext(null);

        Node left = mergeSort(node, comparator);
        Node right = mergeSort(nextOfMiddle, comparator);

        return sortedMerge(left, right, comparator);
    }

    // Merge two sorted lists
    private Node sortedMerge(Node a, Node b, Comparator<T> comparator) {
        if (a == null) return b;
        if (b == null) return a;

        Node result;

        if (comparator.compare((T) a.getData(), (T) b.getData()) <= 0) {
            result = a;
            var nx = sortedMerge(a.getNext(), b, comparator);
            nx.setNext(nx);
        } else {
            result = b;
            var nx = sortedMerge(a, b.getNext(), comparator);
            nx.setNext(nx);
        }

        return result;
    }

    // Find middle of list
    private Node getMiddle(Node node) {
        if (node == null)
            return node;

        Node slow = node;
        Node fast = node.getNext();

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

        Node curr = head;
        while (curr.getNext() != null) {
            curr = curr.getNext();
        }
        tail = curr;
    }
}
