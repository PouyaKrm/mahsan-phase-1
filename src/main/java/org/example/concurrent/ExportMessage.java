package org.example.concurrent;

import org.example.library.constants.LibraryOperationType;

import java.nio.file.Path;

public class ExportMessage extends ConcurrentMessage{

    private final Path folderPath;

    public ExportMessage(Path filePath) {
        this.folderPath = filePath;
    }


    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.EXPORT;
    }

    public Path getFolderPath() {
        return folderPath;
    }
}
