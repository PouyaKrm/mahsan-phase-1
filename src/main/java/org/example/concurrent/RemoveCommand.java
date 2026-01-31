package org.example.concurrent;

import org.example.constansts.LibraryOperationType;
import org.example.constansts.ResourceType;
import org.example.library.Library;

import java.sql.SQLException;

public class RemoveCommand extends LibraryCommand {

    private final Long id;
    private final ResourceType resourceType;

    public RemoveCommand(Library library, Long id, ResourceType resourceType) {
        super(library);
        this.resourceType = resourceType;
        this.id = id;
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.REMOVE;
    }

    @Override
    public void execute() {
        try {
            library.removeItem(id, resourceType);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
