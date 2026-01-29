package org.example.concurrent;

import org.example.constansts.LibraryOperationType;

public class ExitMessage extends ConcurrentMessage{
    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.END;
    }
}
