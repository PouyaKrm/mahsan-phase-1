package org.example.concurrent;

import org.example.constansts.LibraryOperationType;
import org.example.importer.BookImporter;
import org.example.library.Library;
import org.example.library.model.book.Book;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

public class FileImportCommand extends LibraryCommand {
    private final Path filePath;
    private final BookImporter bookImporter;

    public FileImportCommand(Library library, Path filePath, BookImporter bookImporter) {
        super(library);
        this.filePath = filePath;
        this.bookImporter = bookImporter;
    }

    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.FILE;
    }

    @Override
    public void execute() {
        try (var file = new FileInputStream(filePath.toString())) {
            var bs = bookImporter.getModels(file, Book.class);
            library.addAll(bs);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
