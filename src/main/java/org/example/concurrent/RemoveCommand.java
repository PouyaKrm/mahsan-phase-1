package org.example.concurrent;

import org.example.constansts.LibraryOperationType;
import org.example.exception.ItemNotFoundException;
import org.example.library.Library;
import org.example.library.model.BaseLibraryModel;

public class RemoveCommand extends LibraryCommand {

    private final Long id;
    private final Class<? extends BaseLibraryModel> tclass;

    public RemoveCommand(Library library, Long id, Class<? extends BaseLibraryModel> tclass) {
        super(library);
        this.tclass = tclass;
        this.id = id;
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.REMOVE;
    }

    @Override
    public void execute() {

        try {
            library.removeItem(id, tclass);
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        }

    }
}
