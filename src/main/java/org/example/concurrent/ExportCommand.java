package org.example.concurrent;

import org.example.constansts.LibraryOperationType;
import org.example.importer.BookImporter;
import org.example.library.Library;

import java.io.IOException;
import java.nio.file.Path;

public class ExportCommand extends LibraryCommand {

    private final Path folderPath;
    private final BookImporter bookImporter;

    public ExportCommand(Library library, Path filePath, BookImporter bookImporter) {
        super(library);
        this.folderPath = filePath;
        this.bookImporter = bookImporter;
    }


    @Override
    public LibraryOperationType getOperationType() {
        return LibraryOperationType.EXPORT;
    }

    @Override
    public void execute() {
        try {
            bookImporter.writeToFile(library.getAllBooks(), folderPath, "books.txt");
            bookImporter.writeToFile(library.getAllArticles(), folderPath, "articles.txt");
            bookImporter.writeToFile(library.getAllMagazines(), folderPath, "magazines.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
