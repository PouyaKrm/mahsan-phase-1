package org.example.concurrent;

import org.example.library.constants.LibraryOperationType;

public class ShowBorrowedMessage extends ConcurrentMessage {

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.SHOW_BORROWED;
    }
}
