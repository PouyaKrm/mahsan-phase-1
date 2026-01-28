package org.example.concurrent;

import org.example.library.constants.LibraryOperationType;

import java.nio.file.Path;

public class FileImport extends ConcurrentMessage {
    private final Path filePath;
    public FileImport(Path filePath) {
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
