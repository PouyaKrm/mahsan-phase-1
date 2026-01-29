package org.example.concurrent;

import org.example.constansts.LibraryOperationType;

public class ReturnMessage extends ConcurrentMessage {
    private final String title;
    public ReturnMessage(String title) {
        this.title = title;
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.RETURN;
    }

    public String getTitle() {
        return title;
    }
}

