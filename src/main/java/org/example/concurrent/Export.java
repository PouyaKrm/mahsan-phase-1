package org.example.concurrent;

import org.example.library.constants.LibraryOperationType;

import java.nio.file.Path;

public class Export extends ConcurrentMessage{

    private final Path folderPath;

    public Export(Path filePath) {
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
