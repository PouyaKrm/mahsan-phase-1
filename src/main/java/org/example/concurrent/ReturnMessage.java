package org.example.concurrent;

import org.example.library.constants.LibraryOperationType;

public class ReturnMessage extends ConcurrentMessage {
    private final Long id;
    public ReturnMessage(Long id) {
        this.id = id;
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.RETURN;
    }

    public Long getId() {
        return id;
    }
}

