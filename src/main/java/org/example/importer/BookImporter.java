package org.example.importer;

import org.example.library.Book;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public interface BookImporter {
    Book[] getBooks(Scanner scanner, String delimeter, String dateFormat) ;
    Book[] getBooks(Scanner scanner, String delimeter, String dateFormat, String terminationLine) ;
}
