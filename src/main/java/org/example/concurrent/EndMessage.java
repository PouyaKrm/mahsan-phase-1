package org.example.concurrent;

import org.example.library.constants.LibraryOperationType;

public class EndMessage extends ConcurrentMessage{
    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.END;
    }
}
