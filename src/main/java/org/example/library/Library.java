package org.example.library;

import org.example.constansts.ResourceType;
import org.example.constansts.SearchOperation;
import org.example.constansts.SearchField;
import org.example.exception.ItemNotFoundException;
import org.example.library.collection.ArrayList;
import org.example.library.collection.LibraryCollection;
import org.example.library.dto.SearchDTO;
import org.example.library.model.*;
import org.example.library.model.article.Article;
import org.example.library.model.book.Book;
import org.example.library.model.book.BookRepository;
import org.example.library.model.book.BookRepositoryImpl;
import org.example.library.model.magazine.Magazine;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

public class Library {

    private final LibraryCollection<Book> bookCollection = new ArrayList();
    private final LibraryCollection<Article> articles = new ArrayList<>();
    private final LibraryCollection<Magazine> magazines = new ArrayList<>();

    private final BookRepository bookRepository = BookRepositoryImpl.getInstance();

    public <T extends BaseModel> void addItem(T book) throws SQLException {
        switch (book.resourceType()) {
            case BOOK -> {
                bookCollection.add((Book) book);
                bookRepository.save((Book) book);
            }
            case ARTICLE -> articles.add((Article) book);
            case MAGAZINE -> magazines.add((Magazine) book);
        }
    }


    public <T extends BaseModel> void removeItem(T book) throws SQLException {
        switch (book.resourceType()) {
            case BOOK -> {
                bookCollection.remove((Book) book);
                bookRepository.removeOne((Book) book);
            }
            case ARTICLE -> articles.remove((Article) book);
            case MAGAZINE -> magazines.remove((Magazine) book);
        }
    }


    public <T extends BaseModel> T removeItem(Long id, ResourceType resourceType) throws SQLException {
        Predicate<BaseModel> pr = model -> model.getId().equals(id);
        switch (resourceType) {
            case BOOK -> {
                bookRepository.removeOne(id);
                return (T) bookCollection.remove(model -> model.getId().equals(id));
            }
            case ARTICLE -> articles.remove(article -> article.getId().equals(id));
            case MAGAZINE -> magazines.remove(magazine -> magazine.getId().equals(id));

        }

        return null;
    }

    public BaseModel[] search(List<SearchDTO> searchDTOS) {
        var bookSearch = bookCollection.search(getPredicates(searchDTOS), Book.class);
        var magazineSearch = magazines.search(getPredicates(searchDTOS), Magazine.class);
        var articleSearch = articles.search(getPredicates(searchDTOS), Article.class);
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

    public Book[] getAllBooks() {
        return bookCollection.getItems(Book.class);
    }

    public Article[] getAllArticles() {
        return articles.getItems(Article.class);
    }

    public Magazine[] getAllMagazines() {
        return magazines.getItems(Magazine.class);
    }

    public <T extends BaseModel> void addAll(T[] books) throws SQLException {
        for (var book : books) {
            addItem(book);
        }
    }

    private <T extends BaseModel> Predicate<T> getPredicates(List<SearchDTO> searchDTOS) {
        Predicate<T> predicate = (b) -> true;
        for (var field : searchDTOS) {
            Predicate<BaseModel> pr = getPredicate(field);
            predicate = predicate.and(pr);
        }
        return predicate;
    }

    private <T extends BaseModel> Predicate<T> getPredicate(SearchDTO dto) {
        switch (dto.field()) {
            case ID:
                return (T book) -> book.getId().toString().equals(dto.value());
            case TITLE:
                return dto.operation() == SearchOperation.EQ ? (T book) -> book.getTitle().equals(dto.value()) : (T book) -> book.getTitle().contains(dto.value());
            case AUTHOR:
                return dto.operation() == SearchOperation.EQ ? (T book) -> book.getAuthor().equals(dto.value()) : (T book) -> book.getAuthor().contains(dto.value());
            case RESOURCE_TYPE:
                return (T book) -> book.resourceType().toString().equals(dto.value());
            case STATUS:
                return (T book) -> book.resourceType().equals(ResourceType.BOOK) && ((Book) book).getStatus().toString().equals(dto.value());
            default:
                return (T book) -> false;
        }

    }

    public BaseModel borrowItem(Long id) throws ItemNotFoundException, SQLException {
        List<SearchDTO> searchDTOS = new java.util.ArrayList<>();
        searchDTOS.add(new SearchDTO(SearchField.RESOURCE_TYPE, ResourceType.BOOK.toString(), SearchOperation.EQ));
        searchDTOS.add(new SearchDTO(SearchField.ID, id.toString(), SearchOperation.EQ));
        searchDTOS.add(new SearchDTO(SearchField.STATUS, Book.Status.EXIST.toString(), SearchOperation.EQ));
        var result = search(searchDTOS);
        if (result.length == 0) {
            throw new ItemNotFoundException("item not found");
        }
        var item = result[0];
        item.setBorrowDate(LocalDate.now());
        ((Book) item).setStatus(Book.Status.BORROWED);
        bookRepository.save((Book) item);
        return item;
    }

    public BaseModel returnItem(Long id) throws ItemNotFoundException, SQLException {
        List<SearchDTO> searchDTOS = new java.util.ArrayList<>();
        searchDTOS.add(new SearchDTO(SearchField.RESOURCE_TYPE, ResourceType.BOOK.toString(), SearchOperation.EQ));
        searchDTOS.add(new SearchDTO(SearchField.ID, id.toString(), SearchOperation.EQ));
        searchDTOS.add(new SearchDTO(SearchField.STATUS, Book.Status.BORROWED.toString(), SearchOperation.EQ));
        var result = search(searchDTOS);
        if (result.length == 0) {
            throw new ItemNotFoundException("item not found");
        }
        var item = result[0];
        ((Book) item).setStatus(Book.Status.EXIST);
        bookRepository.save((Book) item);
        return item;
    }

    public BaseModel[] getBorrowedItems() {
        List<SearchDTO> searchDTOS = new java.util.ArrayList<>();
        searchDTOS.add(new SearchDTO(SearchField.RESOURCE_TYPE, ResourceType.BOOK.toString(), SearchOperation.EQ));
        searchDTOS.add(new SearchDTO(SearchField.STATUS, Book.Status.BORROWED.toString(), SearchOperation.EQ));
        return search(searchDTOS);
    }

    public void initialize() {
        try {
            var books = bookRepository.getAll();
            bookCollection.addAll(books);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
