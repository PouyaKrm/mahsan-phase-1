package org.example.concurrent;

import org.example.library.constants.LibraryOperationType;

public abstract class ConcurrentMessage {

    public abstract LibraryOperationType getOperationType();
}
