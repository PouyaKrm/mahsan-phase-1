package org.example.concurrent;

import org.example.constansts.LibraryOperationType;
import org.example.library.Library;

public class ExitCommand extends LibraryCommand {
    public ExitCommand(Library library) {
        super(library);
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.END;
    }

    @Override
    public void execute() {

    }
}
