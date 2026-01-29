package org.example.cli;

import org.example.concurrent.*;
import org.example.exception.ItemNotFoundException;
import org.example.constansts.LibraryOperationType;
import org.example.library.model.BaseModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class LibraryCLI {

    private final Scanner scanner = new Scanner(System.in);
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final BlockingQueue<ConcurrentMessage> messages = new LinkedBlockingQueue<>();

    public void start() throws IOException, ItemNotFoundException {
        executorService.execute(new LibraryRunnable(messages));
        System.out.println("Welcome to the Library CLI");
        while (true) {
            System.out.println("1.File import,  2.Direct Input from terminal, 3.Search, 4.Export, 5.borrow book, 6.Show borrow 7.return item");
            var op = getMainOptions();
            try {
                handleOption(op);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private LibraryOperationType getMainOptions() {
        Optional<Integer> option = Optional.empty();
        while (option.isEmpty()) {
            System.out.println("Enter selected option: ");
            option = getNumberOption();
        }
        return LibraryOperationType.fromValue(option.get());
    }

    private void handleOption(LibraryOperationType options) throws IOException, ItemNotFoundException, InterruptedException {
        switch (options) {
            case FILE -> importBooks(options);
            case STDIN -> importBooks(options);
            case BORROW -> borrowBook();
            case SHOW_BORROWED -> messages.put(new ShowBorrowedMessage());
            case SEARCH -> searchBook();
            case RETURN -> returnBook();
            default -> writeToFile();
        }
    }


    private void searchBook() throws InterruptedException {
        var terms = getSearchTerms();
        messages.put(new SearchMessage(terms));
    }

    private String[][] getSearchTerms() {
        while (true) {
            System.out.println("Enter search term comma seperated(<field name>  <value>)");
            var searchTermsStr = scanner.nextLine();
            var searchTermsStream = Arrays.stream(searchTermsStr.split(",")).map(item -> item.split(" "));
            var allMatch = searchTermsStream.allMatch(term -> term.length == 3);
            if (allMatch) {
                return Arrays.stream(searchTermsStr.split(",")).map(item -> item.split(" ")).toArray(String[][]::new);
            }
        }

    }

    private void returnBook() throws ItemNotFoundException, InterruptedException {
        System.out.print("Enter book title: ");
        var title = scanner.nextLine();
        messages.put(new ReturnMessage(title));
    }

    private Optional<Integer> getNumberOption() {
        var p = scanner.nextLine();
        var vals = Arrays.stream(LibraryOperationType.values()).map(item -> item.getValue()).toList();
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


    private void fileImport() throws FileNotFoundException, InterruptedException {
        System.out.println("Enter file path: ");
        var filePath = scanner.nextLine();
        messages.put(new FileImportMessage(Path.of(filePath)));
    }

    private void importBooks(LibraryOperationType option) throws IOException, InterruptedException {
        if (option == LibraryOperationType.FILE) {
            fileImport();
        }
    }

    private void borrowBook() throws InterruptedException {
        System.out.print("Enter book title: ");
        var title = scanner.nextLine();
        messages.put(new BorrowMessage(title));
    }

    private <T extends BaseModel> void writeToFile() throws IOException, InterruptedException {
        var folderPath = getFilePath();
        messages.put(new ExportMessage(folderPath));
    }

    private Path getFilePath() {
        while (true) {
            try {
                System.out.print("Enter folder path: ");
                var path = scanner.nextLine();
                return Path.of(path).toAbsolutePath();
            } catch (InvalidPathException e) {
                continue;
            }

        }
    }

}
