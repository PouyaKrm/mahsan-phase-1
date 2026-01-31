package org.example.concurrent;

import org.example.constansts.LibraryOperationType;
import org.example.library.Library;

import java.util.Arrays;

public class ShowBorrowedCommand extends LibraryCommand {

    public ShowBorrowedCommand(Library library) {
        super(library);
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.SHOW_BORROWED;
    }

    @Override
    public void execute() {
        Arrays.stream(library.getBorrowedItems()).toList().forEach(item -> item.display());
    }
}
