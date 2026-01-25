package org.example.importer;

import org.example.library.model.Book;

import java.util.Scanner;

public interface BookImporter {
    Book[] getBooks(Scanner scanner, String delimeter, String dateFormat) ;
    Book[] getBooks(Scanner scanner, String delimeter, String dateFormat, String terminationLine) ;
}
