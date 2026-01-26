package org.example.cli;

import org.example.constansts.ResourceType;
import org.example.exception.ItemNotFoundException;
import org.example.importer.BookImporter;
import org.example.importer.BookImporterImpl;
import org.example.library.Library;
import org.example.library.model.BaseModel;
import org.example.library.model.ModelFactory;
import org.example.library.model.article.ArticleFactory;
import org.example.library.model.book.Book;
import org.example.library.model.book.BookFactory;
import org.example.library.model.magazine.MagazineFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class LibraryCLI {

    private final Scanner scanner = new Scanner(System.in);
    private final BookImporter bookImporter = new BookImporterImpl();
    private final Library library = new Library();
    private final Map<ResourceType, ModelFactory<? extends BaseModel>> factories = Map.ofEntries(
            Map.entry(ResourceType.BOOK, BookFactory.getFactory()),
            Map.entry(ResourceType.ARTICLE, ArticleFactory.getFactory()),
            Map.entry(ResourceType.MAGAZINE, MagazineFactory.getFactory())
    );

    public void start() throws IOException {
        System.out.println("Welcome to the Library CLI");
        while(true) {
            System.out.println("1.File import,  2.Direct Input from terminal, 3.Search by title, 4.Export, 5.borrow book, 6.Show borrow");
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
        switch (options) {
            case FILE:
            case STDIN:
                var bs = importBooks(options);
                library.addAll(bs);
                break;
            case BORROW:
                borrowBook();
                break;
            case SHOW_BORROWED:
                System.out.println(library.getBorrowedItems());
                break;
            case Options.SEARCH:
                searchBook();
                break;
            default:
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
        EXPORT(4),
        BORROW(5),
        SHOW_BORROWED(6),;
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
            var bs = bookImporter.getModels(fileScanner, ResourceType.BOOK, Book.class);
            fileScanner.close();
            return bs;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Book[] direcImport() {
        System.out.println("Enter each book line by line(use \\q to exit): ");
        return bookImporter.getModels(scanner, ResourceType.BOOK, Book.class, "\\q");
    }

    private Book[] importBooks(Options option) {
        if (option == Options.FILE) {
            return fileImport();
        }
        return direcImport();
    }

    private void borrowBook() {
        System.out.print("Enter book title: ");
        var title = scanner.nextLine();
        try {
            var item = library.borrowItem(title);
            item.display();
        } catch (ItemNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private <T extends BaseModel> void writeToFile(T[] books) throws IOException {
        try(FileWriter fileWriter = new FileWriter("src/main/resources/output.txt")) {
            for (T book : books) {
                ModelFactory factory = factories.get(book.resourceType());
                var line = factory.parseModelToString(book);
                fileWriter.write(line);
                fileWriter.write("\n");
            }
        }
    }

}
