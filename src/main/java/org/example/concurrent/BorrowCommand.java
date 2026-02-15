package org.example.concurrent;

import org.example.constansts.LibraryOperationType;
import org.example.exception.InvalidOperationException;
import org.example.exception.ItemNotFoundException;
import org.example.library.Library;

import java.sql.SQLException;

public class BorrowCommand extends LibraryCommand {
    private final Long id;
    public BorrowCommand(Library library, Long id) {
        super(library);
        this.id = id;
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.BORROW;
    }

    @Override
    public void execute() {
        try {
            var item = library.borrowItem(id);
            item.display();
        } catch (ItemNotFoundException | InvalidOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}
