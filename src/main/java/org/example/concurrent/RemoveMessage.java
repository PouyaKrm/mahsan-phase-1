package org.example.concurrent;

import org.example.constansts.ResourceType;
import org.example.library.constants.LibraryOperationType;

public class RemoveMessage extends ConcurrentMessage{

    private final Long id;
    private final ResourceType resourceType;

    public RemoveMessage(Long id, ResourceType resourceType) {
        this.resourceType = resourceType;
        this.id = id;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Long getId() {
        return id;
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.REMOVE;
    }
}
