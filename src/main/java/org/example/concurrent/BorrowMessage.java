package org.example.concurrent;

import org.example.constansts.LibraryOperationType;

public class BorrowMessage extends ConcurrentMessage {
    private final Long id;
    public BorrowMessage(Long id) {
        this.id = id;
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.BORROW;
    }

    public Long getId() {
        return id;
    }
}
