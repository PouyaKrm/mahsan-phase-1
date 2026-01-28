package org.example.concurrent;

import org.example.library.constants.LibraryOperationType;

public class ShowBorrowed extends ConcurrentMessage {

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.SHOW_BORROWED;
    }
}
