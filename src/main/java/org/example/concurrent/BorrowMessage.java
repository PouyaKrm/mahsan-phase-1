package org.example.concurrent;

import org.example.constansts.LibraryOperationType;

public class BorrowMessage extends ConcurrentMessage {
    private final String title;
    public BorrowMessage(String title) {
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
