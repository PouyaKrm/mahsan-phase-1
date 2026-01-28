package org.example.concurrent;

import org.example.library.constants.LibraryOperationType;

public class End extends ConcurrentMessage{
    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.END;
    }
}
