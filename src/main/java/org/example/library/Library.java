package org.example.library;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.example.constansts.SearchField;
import org.example.library.collection.ArrayList;
import org.example.library.collection.LibraryCollection;
import org.example.library.model.*;
import org.example.library.model.article.Article;
import org.example.library.model.book.Book;
import org.example.library.model.magazine.Magazine;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

public class Library {

    private final LibraryCollection<Book> bookCollection = new ArrayList();
    private final LibraryCollection<Article> articles = new ArrayList<>();
    private final LibraryCollection<Magazine> magazines = new ArrayList<>();

    private List<Field> getStringFields(BaseModel model) {
        var clazz = model.getClass();
        var fields = clazz.getDeclaredFields();
        return Arrays.stream(fields).filter(field -> field.getType() == String.class).toList();
    }


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
        var bookSearch = bookCollection.search(getPredicates(fields, Book.class), Book.class);
        var magazineSearch = magazines.search(getPredicates(fields, Magazine.class), Magazine.class);
        var articleSearch  = articles.search(getPredicates(fields, Article.class), Article.class);
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

    private <T extends BaseModel> Predicate<T> getPredicates(Map<SearchField, String> fields, Class<T> clazz) {
        Predicate<T> predicate = (b) -> true;
        for (var field : fields.keySet()) {
            Predicate<BaseModel> pr;
            if(field == SearchField.TITLE) {
                pr = (book) -> book.getTitle().contains(fields.get(field));
            } else {
                pr = (book) -> book.getAuthor().contains(fields.get(field));
            }
            predicate = predicate.and(pr);
        }
        return predicate;
    }

}
