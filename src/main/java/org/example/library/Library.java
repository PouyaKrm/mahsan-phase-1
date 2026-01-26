package org.example.library;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.example.library.collection.BookArrayList;
import org.example.library.collection.LibraryCollection;
import org.example.library.model.BaseModel;
import org.example.library.model.Book;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Library {

    private final LibraryCollection bookCollection = new BookArrayList();
    private final Map<String, PatriciaTrie<BaseModel>> items = new HashMap();

    private List<Field> getStringFields(BaseModel model) {
        var clazz = model.getClass();
        var fields = clazz.getDeclaredFields();
        return Arrays.stream(fields).filter(field -> field.getType() == String.class).toList();
    }


    public <T extends BaseModel> void addItem(T book) throws IllegalAccessException {
        var fields = getStringFields(book);
        for (var field : fields) {
            var name = field.getName();
            var trie = items.getOrDefault(name, new PatriciaTrie<>());
            trie.put((String) field.get(book), book);
        }
    }


    public <T extends BaseModel> void removeItem(T book) throws IllegalAccessException {
        var fields = getStringFields(book);
        for (var field : fields) {
            var name = field.getName();
            var trie = items.getOrDefault(name, new PatriciaTrie<>());
            trie.remove((String) field.get(book), book);
        }
    }

    public <T extends BaseModel> T[] search(Predicate<T> predicate) {
        return (T[]) bookCollection.search(predicate, Book.class);
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
        return (T[]) bookCollection.getItems(Book.class);
    }

    public <T extends BaseModel> void addAll(T[] books) {
        bookCollection.addAll(books);
    }
}
