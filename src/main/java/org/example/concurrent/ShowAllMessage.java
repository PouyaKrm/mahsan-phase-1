package org.example.concurrent;

import org.example.constansts.LibraryOperationType;

public class ShowAllMessage extends ConcurrentMessage{

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.SHOW_ALL;
    }
}
