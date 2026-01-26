package org.example.library;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.example.constansts.ResourceType;
import org.example.constansts.SearchField;
import org.example.exception.ItemNotFoundException;
import org.example.library.collection.ArrayList;
import org.example.library.collection.LibraryCollection;
import org.example.library.model.*;
import org.example.library.model.article.Article;
import org.example.library.model.book.Book;
import org.example.library.model.magazine.Magazine;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

public class Library {

    private final LibraryCollection<Book> bookCollection = new ArrayList();
    private final LibraryCollection<Article> articles = new ArrayList<>();
    private final LibraryCollection<Magazine> magazines = new ArrayList<>();


    public <T extends BaseModel> void addItem(T book) {
        switch (book.resourceType()) {
            case BOOK -> bookCollection.add((Book) book);
            case ARTICLE -> articles.add((Article) book);
            case MAGAZINE -> magazines.add((Magazine) book);
        }
    }


    public <T extends BaseModel> void removeItem(T book) {
        switch (book.resourceType()) {
            case BOOK -> bookCollection.remove((Book) book);
            case ARTICLE -> articles.remove((Article) book);
            case MAGAZINE -> magazines.remove((Magazine) book);
        }
    }

    public BaseModel[] search(Map<SearchField, String> fields) {
        var bookSearch = bookCollection.search(getPredicates(fields), Book.class);
        var magazineSearch = magazines.search(getPredicates(fields), Magazine.class);
        var articleSearch = articles.search(getPredicates(fields), Article.class);
        var result = new BaseModel[bookSearch.length + magazineSearch.length + articleSearch.length];
        System.arraycopy(bookSearch, 0, result, 0, bookSearch.length);
        System.arraycopy(magazineSearch, 0, result, bookSearch.length, magazineSearch.length);
        System.arraycopy(articleSearch, 0, result, bookSearch.length + magazineSearch.length, articleSearch.length);
        return result;
    }

    public <T extends BaseModel> void sortByPublicationDate() {
        bookCollection.sort(Comparator.comparingLong(book -> ((BaseModel) book).getPubDate().toEpochDay()));
    }

    public BaseModel[] getAll() {
        var result = new BaseModel[bookCollection.size() + magazines.size() + articles.size()];
        System.arraycopy(bookCollection.getItems(Book.class), 0, result, 0, bookCollection.size());
        System.arraycopy(magazines.getItems(Magazine.class), 0, result, bookCollection.size(), magazines.size());
        System.arraycopy(articles.getItems(Article.class), 0, result, bookCollection.size() + magazines.size(), articles.size());
        return result;
    }

    public <T extends BaseModel> void addAll(T[] books) {
        for (var book : books) {
            addItem(book);
        }
    }

    private <T extends BaseModel> Predicate<T> getPredicates(Map<SearchField, String> fields) {
        Predicate<T> predicate = (b) -> true;
        for (var field : fields.keySet()) {
            Predicate<BaseModel> pr;
            if (field == SearchField.TITLE) {
                pr = (book) -> book.getTitle().contains(fields.get(field));
            } else if (field == SearchField.AUTHOR) {
                pr = (book) -> book.getAuthor().contains(fields.get(field));
            } else {
                pr = (book) -> book.resourceType().equals(fields.get(field));
            }
            predicate = predicate.and(pr);
        }
        return predicate;
    }

    public BaseModel borrowItem(String title) throws ItemNotFoundException {
        Map<SearchField, String> fields = new HashMap();
        fields.put(SearchField.RESOURCE_TYPE, ResourceType.BOOK.toString());
        fields.put(SearchField.TITLE, title);
        fields.put(SearchField.Status, Book.Status.EXIST.toString());
        var result = search(fields);
        if(result.length == 0) {
            throw new ItemNotFoundException("item not found");
        }
        var item = result[0];
        item.setBorrowDate(LocalDate.now());
        ((Book) item).setStatus(Book.Status.BORROWED);
        return item;
    }

    public BaseModel returnItem(BaseModel item) throws ItemNotFoundException {
        Map<SearchField, String> fields = new HashMap();
        fields.put(SearchField.RESOURCE_TYPE, ResourceType.BOOK.toString());
        fields.put(SearchField.TITLE, item.getTitle());
        fields.put(SearchField.Status, Book.Status.BORROWED.toString());
        var result = search(fields);
        if(result.length == 0) {
            throw new ItemNotFoundException("item not found");
        }
        ((Book) item).setStatus(Book.Status.EXIST);
        return item;
    }

    public BaseModel[] getBorrowedItems() {
        Map<SearchField, String> fields = new HashMap();
        fields.put(SearchField.RESOURCE_TYPE, ResourceType.BOOK.toString());
        fields.put(SearchField.TITLE, Book.Status.BORROWED.toString());
        return search(fields);
    }
}
