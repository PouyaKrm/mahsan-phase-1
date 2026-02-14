package org.example.library;

import org.example.constansts.ResourceType;
import org.example.constansts.SearchField;
import org.example.constansts.SearchOperation;
import org.example.exception.ItemNotFoundException;
import org.example.library.collection.ArrayList;
import org.example.library.collection.LibraryCollection;
import org.example.library.dto.SearchDTO;
import org.example.library.model.BaseLibraryModel;
import org.example.library.model.library.article.Article;
import org.example.library.model.library.book.Book;
import org.example.library.model.library.magazine.Magazine;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

public class InMemoryLibraryImpl implements Library {

    private final LibraryCollection<Book> bookCollection = new ArrayList<>();
    private final LibraryCollection<Article> articles = new ArrayList<>();
    private final LibraryCollection<Magazine> magazines = new ArrayList<>();
    Map<Class<? extends BaseLibraryModel>, LibraryCollection<? extends BaseLibraryModel>> collectionMap = Map.ofEntries(
            Map.entry(Book.class, bookCollection),
            Map.entry(Article.class, new ArrayList<>()),
            Map.entry(Magazine.class, new ArrayList<>())
    );

    @SuppressWarnings("unchecked")
    private <T extends BaseLibraryModel> LibraryCollection<T> getCollection(Class<T> tClass) {
        return (LibraryCollection<T>) collectionMap.get(tClass);
    }


    public <T extends BaseLibraryModel> T getItem(Long id, Class<T> tClass) throws ItemNotFoundException {
        var r = getCollection(tClass).search(item -> item.getId().equals(id), tClass);
        if (r.length == 0) {
            throw new ItemNotFoundException("item not found");
        }
        return r[0];
    }


    @Override
    public <T extends BaseLibraryModel> void addItem(T book, Class<T> tClass) {
        var random = new Random();
        if (Objects.isNull(book.getId())) {
            book.setId(random.nextLong());
        }
        getCollection(tClass).add(book);
    }

    @Override
    public <T extends BaseLibraryModel> void removeItem(T book, Class<T> tClass) {
        getCollection(tClass).remove(book);
    }

    @Override
    public <T extends BaseLibraryModel> T removeItem(Long id, Class<T> tClass) throws ItemNotFoundException {
        Predicate<T> pr = model -> model.getId().equals(id);
        return getCollection(tClass).remove(pr);
    }

    @Override
    public BaseLibraryModel[] search(List<SearchDTO> searchDTOS) {
        var bookSearch = bookCollection.search(getPredicates(searchDTOS), Book.class);
        var magazineSearch = magazines.search(getPredicates(searchDTOS), Magazine.class);
        var articleSearch = articles.search(getPredicates(searchDTOS), Article.class);
        var result = new BaseLibraryModel[bookSearch.length + magazineSearch.length + articleSearch.length];
        System.arraycopy(bookSearch, 0, result, 0, bookSearch.length);
        System.arraycopy(magazineSearch, 0, result, bookSearch.length, magazineSearch.length);
        System.arraycopy(articleSearch, 0, result, bookSearch.length + magazineSearch.length, articleSearch.length);
        return result;
    }


    public <T extends BaseLibraryModel> void sortByPublicationDate() {
        bookCollection.sort(Comparator.comparingLong(book -> ((BaseLibraryModel) book).getPubDate().toEpochDay()));
    }

    @Override
    public Book[] getAllBooks() {
        return getCollection(Book.class).getItems(Book.class);
    }

    @Override
    public Article[] getAllArticles() {
        return getCollection(Article.class).getItems(Article.class);
    }

    @Override
    public Magazine[] getAllMagazines() {
        return getCollection(Magazine.class).getItems(Magazine.class);
    }

    @Override
    public <T extends BaseLibraryModel> T[] addAll(T[] books, Class<T> tClass) {
        getCollection(tClass).addAll(books);
        return books;
    }

    private <T extends BaseLibraryModel> Predicate<T> getPredicates(List<SearchDTO> searchDTOS) {
        Predicate<T> predicate = (b) -> true;
        for (var field : searchDTOS) {
            Predicate<BaseLibraryModel> pr = getPredicate(field);
            predicate = predicate.and(pr);
        }
        return predicate;
    }

    private <T extends BaseLibraryModel> Predicate<T> getPredicate(SearchDTO dto) {
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

    @Override
    public BaseLibraryModel borrowItem(Long id) throws ItemNotFoundException {
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
        return item;
    }

    @Override
    public BaseLibraryModel returnItem(Long id) throws ItemNotFoundException {
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
        return item;
    }

    @Override
    public BaseLibraryModel[] getBorrowedItems() {
        List<SearchDTO> searchDTOS = new java.util.ArrayList<>();
        searchDTOS.add(new SearchDTO(SearchField.RESOURCE_TYPE, ResourceType.BOOK.toString(), SearchOperation.EQ));
        searchDTOS.add(new SearchDTO(SearchField.STATUS, Book.Status.BORROWED.toString(), SearchOperation.EQ));
        return search(searchDTOS);
    }

}
