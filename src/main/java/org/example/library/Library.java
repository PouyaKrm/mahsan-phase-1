package org.example.library;

import org.example.library.collection.BookArrayList;
import org.example.library.collection.LibraryCollection;
import org.example.library.model.BaseModel;
import org.example.library.model.Book;

import java.util.*;
import java.util.function.Predicate;

public class Library {

    private final LibraryCollection bookCollection = new BookArrayList();

    public <T extends BaseModel> void addItem(T book) {
        bookCollection.add(book);
    }

    public <T extends BaseModel> void removeItem(T book) {
        bookCollection.remove(book);
    }

    public void printBooks() {
        for (var book : bookCollection.getItems()) {
            System.out.println(book);
        }
    }

    public <T extends BaseModel> T[] search(Predicate<T> predicate) {
        return (T[]) bookCollection.search(predicate);
    }

//    public <T extends BaseModel> T[] findByTitle(String title) {
//        return bookCollection.search(book -> book.getTitle().contains(title));
//    }
//
//    public Optional<Book> findByAuthor(String author) {
//        return bookCollection.search(book -> book.getAuthor().contains(author));
//    }

    public <T extends BaseModel> void sortByPublicationDate() {
        bookCollection.sort(Comparator.comparingLong(book -> ((BaseModel) book).getPubDate().toEpochDay()));
    }

    public <T extends BaseModel> T[] getAll() {
        return (T[]) bookCollection.getItems();
    }

    public <T extends BaseModel> void addAll(T[] books) {
        bookCollection.addAll(books);
    }
}
