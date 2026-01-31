package org.example.concurrent;

import org.example.constansts.LibraryOperationType;
import org.example.library.Library;

public class InitializeCommand extends LibraryCommand {
    public InitializeCommand(Library library) {
        super(library);
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.INITIALIZE;
    }

    @Override
    public void execute() {
        library.initialize();
    }
}
