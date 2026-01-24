package org.example.importer;

import org.example.library.Book;

import java.io.FileNotFoundException;
import java.util.List;

public interface BookImporter {
    Book[] getBooks() throws FileNotFoundException;
}
