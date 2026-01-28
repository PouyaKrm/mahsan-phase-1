package org.example.cli;

import org.example.concurrent.*;
import org.example.constansts.SearchField;
import org.example.constansts.SearchOperation;
import org.example.exception.ItemNotFoundException;
import org.example.importer.BookImporter;
import org.example.importer.JsonBookImporterImpl;
import org.example.library.Library;
import org.example.library.constants.LibraryOperationType;
import org.example.library.dto.SearchDTO;
import org.example.library.model.book.Book;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class LibraryRunnable implements Runnable {

    private final Library library = new Library();
    private final BlockingQueue<ConcurrentMessage> messages;
    private final BookImporter bookImporter = new JsonBookImporterImpl();

    public LibraryRunnable(BlockingQueue<ConcurrentMessage> messages) {
        this.messages = messages;
    }

    @Override
    public void run() {
        try {
            ConcurrentMessage message;
            while (!(message = messages.take()).getOperationType().equals(LibraryOperationType.END)) {
                handleMessage(message);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleMessage(ConcurrentMessage message) {
        switch (message.getOperationType()) {
            case LibraryOperationType.FILE -> fileImport((FileImportMessage) message);
            case LibraryOperationType.BORROW -> borrowBook((BorrowMessage) message);
            case LibraryOperationType.SEARCH -> searchBook((SearchMessage) message);
            case LibraryOperationType.RETURN -> returnBook((ReturnMessage) message);
            case LibraryOperationType.EXPORT -> exportBook((ExportMessage) message);
            case LibraryOperationType.SHOW_BORROWED -> showBorrowed();
        }
    }

    private void fileImport(FileImportMessage fileImport) {
        var filePath = fileImport.getFilePath();
        try (var file = new FileInputStream(filePath.toString())) {
            var bs = bookImporter.getModels(file, Book.class);
            library.addAll(bs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void borrowBook(BorrowMessage message) {
        try {
            var item = library.borrowItem(message.getTitle());
            item.display();
        } catch (ItemNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void searchBook(SearchMessage message) {
        var terms = message.getSearchTerms();
        var searchDtos = Arrays.stream(terms).map(termValue -> {
            var field = SearchField.valueOf(termValue[0]);
            var op = SearchOperation.valueOf(termValue[2]);
            return new SearchDTO(field, termValue[1], op);
        }).toList();
        var result = library.search(searchDtos);
        for (var dto : result) {
            dto.display();
        }
    }

    private void showBorrowed() {
        Arrays.stream(library.getBorrowedItems()).toList().forEach(item -> item.display());
    }

    private void returnBook(ReturnMessage message) {
        try {
            library.returnItem(message.getTitle());
        } catch (ItemNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void exportBook(ExportMessage message) {
        var folderPath = message.getFolderPath();
        try {
            bookImporter.writeToFile(library.getAllBooks(), folderPath, "books.txt");
            bookImporter.writeToFile(library.getAllArticles(), folderPath, "articles.txt");
            bookImporter.writeToFile(library.getAllMagazines(), folderPath, "magazines.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
