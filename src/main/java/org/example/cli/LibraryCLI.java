package org.example.cli;

import org.example.importer.BookImporter;
import org.example.importer.BookImporterImpl;
import org.example.library.Library;
import org.example.library.model.Book;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class LibraryCLI {

    private final Scanner scanner = new Scanner(System.in);
    private final BookImporter bookImporter = new BookImporterImpl();
    private final Library library = new Library();

    public void start() throws IOException {
        System.out.println("Welcome to the Library CLI");
        while(true) {
            System.out.println("1.File import,  2.Direct Input from terminal, 3.search by title, 4.export");
            var op = getMainOptions();
            handleOption(op);
        }
    }

    private Options getMainOptions() {
        Optional<Integer> option = Optional.empty();
        while (option.isEmpty()) {
            System.out.println("Enter selected option: ");
            option = getNumberOption();
        }
        return Options.fromValue(option.get());
    }

    private void handleOption(Options options) throws IOException {
        if(options == Options.FILE || options == Options.STDIN) {
            var bs = importBooks(options);
            library.addAll(bs);
        } else if (options == Options.SEARCH) {
            searchBook();
        } else {
            writeToFile(library.getAll());
        }
    }


    private void searchBook() {
        System.out.println("Enter search term comma seperated(<field name>  <value>)");
            var searchTerm = scanner.nextLine();
            var searchTerms = Arrays.stream(searchTerm.split(","));

    }

    private Optional<Integer> getNumberOption() {
        var p = scanner.nextLine();
        var vals = Arrays.stream(Options.values()).map(item -> item.value).toList();
        try {
            var n = Integer.parseInt(p);
            if (vals.contains(n)) {
                return Optional.of(n);
            }
            return Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static enum Options {
        FILE(1),
        STDIN(2),
        SEARCH(3),
        EXPORT(4);
        private final int value;
        Options(int i) {
            this.value = i;
        }
        static Options fromValue(int i) {
            return Arrays.stream(Options.values()).filter(item -> item.value == i).findFirst().get();
        }
    }

    private Book[] fileImport() {
        System.out.println("Enter file path: ");
        var filePath = scanner.nextLine();
        try {
            var fileReader = new FileReader(filePath);
            var fileScanner = new Scanner(fileReader);
            var bs = bookImporter.getBooks(fileScanner, ",", "dd-MM-yyyy");
            fileScanner.close();
            return bs;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Book[] direcImport() {
        System.out.println("Enter each book line by line(use \\q to exit): ");
        return bookImporter.getBooks(scanner, ",", "dd-MM-yyyy", "\\q");
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
