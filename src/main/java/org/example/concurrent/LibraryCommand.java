package org.example.concurrent;

import org.example.constansts.LibraryOperationType;
import org.example.library.Library;

public abstract class LibraryCommand {

    protected final Library library;

    public LibraryCommand(Library library) {
        this.library = library;
    }

    public abstract LibraryOperationType getOperationType();

    public abstract void execute();
}
