package org.example.concurrent;

import org.example.library.constants.LibraryOperationType;

public class Return extends ConcurrentMessage {
    private final String title;
    public Return(String title) {
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

