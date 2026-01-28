package org.example.concurrent;

import org.example.library.constants.LibraryOperationType;

public class Borrow extends ConcurrentMessage {
    private final String title;
    public Borrow(String title) {
        this.title = title;
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.BORROW;
    }

    public String getTitle() {
        return title;
    }
}
