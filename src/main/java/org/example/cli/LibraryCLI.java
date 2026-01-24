package org.example.cli;

import org.example.importer.BookImporter;
import org.example.importer.BookImporterImpl;
import org.example.library.Book;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class LibraryCLI {

    private final Scanner scanner = new Scanner(System.in);
    private final BookImporter bookImporter = new BookImporterImpl();

    public void start() throws IOException {
        System.out.println("Welcome to the Library CLI");
        System.out.println("1. File import,  2. Direct Input from terminal");
        var op = getMainOptions();
        Book[] books = importBooks(op);
        writeToFile(books);
        scanner.close();
    }

    private Options getMainOptions() {
        Integer option = null;
        while (Objects.isNull(option)) {
            System.out.println("Enter selected option: ");
            option = getNumberOption();
        }
        final Integer fnVal = option;
        return Arrays.stream(Options.values()).filter(item -> item.value == fnVal).findFirst().get();
    }

    private Integer getNumberOption() {
        var p = scanner.nextLine();
        try {
            var n = Integer.parseInt(p);
            if (n == 1 || n == 2) {
                return n;
            }
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static enum Options {

        FILE(1),
        STDIN(2);
        private final int value;

        Options(int i) {
            this.value = i;
        }
    }

    private Book[] fileImport() {
        System.out.println("Enter file path: ");
        var filePath = scanner.nextLine();
        try {
            var fileReader = new FileReader(filePath);
            var fileScanner = new Scanner(fileReader);
            return bookImporter.getBooks(fileScanner, ",", "dd-MM-yyyy");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Book[] direcImport() {
        System.out.println("Enter each book line by line(use CTR + D to exit): ");
        return bookImporter.getBooks(scanner, ",", "dd-MM-yyyy");
    }

    private Book[] importBooks(Options option) {
        if (option == Options.FILE) {
            return fileImport();
        }
        return direcImport();
    }

    private void writeToFile(Book[] books) throws IOException {
        try(FileWriter fileWriter = new FileWriter("src/main/resources/output.txt")) {
            for (Book book : books) {
                var line = MessageFormat.format("{0},{1},{2},{3}", book.getTitle(), book.getAuthor(), book.getPubDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString(), book.getStatus());
                fileWriter.write(line);
                fileWriter.write("\n");
            }
        }
    }

}
