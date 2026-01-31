package org.example.concurrent;

import org.example.constansts.LibraryOperationType;
import org.example.library.Library;

import java.util.Arrays;

public class ShowAllCommand extends LibraryCommand {

    public ShowAllCommand(Library library) {
        super(library);
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.SHOW_ALL;
    }

    @Override
    public void execute() {
        Arrays.stream(library.getAll()).toList().forEach(item -> item.display());
    }
}
