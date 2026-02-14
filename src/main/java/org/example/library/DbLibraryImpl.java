package org.example.library;

import org.example.constansts.ResourceType;
import org.example.exception.ItemNotFoundException;
import org.example.library.dto.SearchDTO;
import org.example.library.model.BaseModel;
import org.example.library.model.ModelRepository;
import org.example.library.model.article.Article;
import org.example.library.model.article.ArticleRepositoryImpl;
import org.example.library.model.book.Book;
import org.example.library.model.book.BookRepository;
import org.example.library.model.book.BookRepositoryImpl;
import org.example.library.model.magazine.Magazine;
import org.example.library.model.magazine.MagazineRepositoryImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DbLibraryImpl implements Library {

    private final BookRepository bookRepository = BookRepositoryImpl.getInstance();

    private final Map<Class<? extends BaseModel>, ModelRepository<?>> repositoryMap = Map.ofEntries(
            Map.entry(Book.class, bookRepository),
            Map.entry(Article.class, ArticleRepositoryImpl.getInstance()),
            Map.entry(Magazine.class, MagazineRepositoryImpl.getInstance())
    );


    @Override
    public <T extends BaseModel> void addItem(T book, Class<T> tClass) {
        try {
            getRepository(tClass).save(book);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public <T extends BaseModel> void removeItem(T book, Class<T> tClass) {
        try {
            getRepository(tClass).removeOne(book);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public <T extends BaseModel> T removeItem(Long id, Class<T> tClass) throws ItemNotFoundException {
        try {
            var item = getRepository(tClass).getOne(id);
            getRepository(tClass).removeOne(id);
            return (T) item;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends BaseModel> void addItem(T book) {

    }

    @Override
    public <T extends BaseModel> void removeItem(T book) {

    }

    @Override
    public <T extends BaseModel> T removeItem(Long id, ResourceType resourceType) {
        return null;
    }

    @Override
    public BaseModel[] search(List<SearchDTO> searchDTOS) {
        return new BaseModel[0];
    }

    @Override
    public BaseModel[] getAll() {
        return new BaseModel[0];
    }

    public <T extends BaseModel> T[] getAll(Class<T> tClass) {
        try {
            return getRepository(tClass).getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book[] getAllBooks() {
        try {
            return getRepository(Book.class).getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Article[] getAllArticles() {
        try {
            return getRepository(Article.class).getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Magazine[] getAllMagazines() {
        try {
            return getRepository(Magazine.class).getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BaseModel borrowItem(Long id) throws ItemNotFoundException {
        return null;
    }


    @Override
    public <T extends BaseModel> void addAll(T[] books) {

    }


    @Override
    public <T extends BaseModel> T getItem(Long id, Class<T> tClass) throws ItemNotFoundException {
        try {
            return getRepository(tClass).getOne(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book returnItem(Long id) throws ItemNotFoundException {
        var item = getItem(id, Book.class);
        if (!item.getStatus().equals(Book.Status.BORROWED)) {
            throw new ItemNotFoundException("item not found");
        }
        item.setStatus(Book.Status.EXIST);
        try {
            getRepository(Book.class).save(item);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return item;
    }


    @Override
    public BaseModel[] getBorrowedItems() {
        return new BaseModel[0];
    }


    @Override
    public <T extends BaseModel> T[] addAll(T[] books, Class<T> tClass) {
        try {
            return getRepository(tClass).saveAll(books, tClass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseModel> ModelRepository<T> getRepository(Class<T> tClass) {
        return (ModelRepository<T>) repositoryMap.get(tClass);
    }
}
