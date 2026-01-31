package org.example.cli;

import org.example.concurrent.*;
import org.example.constansts.LibraryOperationType;
import org.example.constansts.ResourceType;
import org.example.exception.ItemNotFoundException;
import org.example.importer.BookImporter;
import org.example.importer.JsonBookImporterImpl;
import org.example.library.Library;
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
    private final BlockingQueue<LibraryCommand> commands = new LinkedBlockingQueue<>();
    private final Library library = new Library();
    private final BookImporter bookImporter = new JsonBookImporterImpl();

    public void start() throws IOException, ItemNotFoundException, InterruptedException {
        executorService.execute(new LibraryRunnable(commands));
        commands.put(new InitializeCommand(library));
        System.out.println("Welcome to the Library CLI");
        LibraryOperationType libraryOperationType = null;
        while (Objects.isNull(libraryOperationType) || libraryOperationType != LibraryOperationType.END) {
            showMainMenu();
            libraryOperationType = getMainOptions();
            try {
                handleOption(libraryOperationType);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void showMainMenu() {
        var str = LibraryOperationType.getSelectableOptions().stream().sorted(Comparator.comparingInt(LibraryOperationType::getValue)).filter(item -> item.getValue() != 0)
                .map(item -> item.getValue() + "." + item.getTitle() + " ").reduce((start, item) -> start + " " + item).get();
        System.out.println(str);

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
            case SHOW_BORROWED -> commands.put(new ShowBorrowedCommand(library));
            case SEARCH -> searchBook();
            case RETURN -> returnBook();
            case REMOVE -> removeItem();
            case EXPORT -> writeToFile();
            case SHOW_ALL -> commands.put(new ShowAllCommand(library));
            case END -> exit();
        }
    }


    private void searchBook() throws InterruptedException {
        var terms = getSearchTerms();
        commands.put(new SearchCommand(library, terms));
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
        var id = getLongFromInput("Enter item id: ");
        commands.put(new ReturnCommand(library, id));
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
        commands.put(new FileImportCommand(library, Path.of(filePath), bookImporter));
    }

    private void importBooks(LibraryOperationType option) throws IOException, InterruptedException {
        if (option == LibraryOperationType.FILE) {
            fileImport();
        }
    }

    private void borrowBook() throws InterruptedException {
        var id = getLongFromInput("Enter item id: ");
        commands.put(new BorrowCommand(library, id));
    }

    private <T extends BaseModel> void writeToFile() throws IOException, InterruptedException {
        var folderPath = getFilePath();
        commands.put(new ExportCommand(library, folderPath, bookImporter));
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

    private void removeItem() throws InterruptedException {
        var id = getLongFromInput("Enter item id: ");
        commands.put(new RemoveCommand(library, id, ResourceType.BOOK));
    }

    private void exit() throws InterruptedException {
        commands.put(new ExitCommand(library));
        executorService.shutdown();
    }


    private Long getLongFromInput(String message) {
        Long id = null;
        while (Objects.isNull(id)) {
            System.out.println(message);
            try {
                id = Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return id;
    }
}
