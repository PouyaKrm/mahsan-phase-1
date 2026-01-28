package org.example.concurrent;

import org.example.library.constants.LibraryOperationType;

import java.nio.file.Path;

public class FileImportMessage extends ConcurrentMessage {
    private final Path filePath;
    public FileImportMessage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.FILE;
    }

    public Path getFilePath() {
        return filePath;
    }
}
