package org.example.concurrent;

import org.example.constansts.LibraryOperationType;
import org.example.exception.ItemNotFoundException;
import org.example.library.Library;

import java.sql.SQLException;

public class ReturnCommand extends LibraryCommand {
    private final Long id;
    public ReturnCommand(Library library, Long id) {
        super(library);
        this.id = id;
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.RETURN;
    }

    @Override
    public void execute() {
        try {
            library.returnItem(id);
        } catch (ItemNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}

