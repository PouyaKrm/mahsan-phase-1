package org.example.library;

import org.example.exception.ItemNotFoundException;
import org.example.library.dto.SearchDTO;
import org.example.library.model.BaseLibraryModel;
import org.example.library.model.library.LibraryModelRepository;
import org.example.library.model.library.article.Article;
import org.example.library.model.library.article.ArticleRepositoryImpl;
import org.example.library.model.library.book.Book;
import org.example.library.model.library.book.BookRepository;
import org.example.library.model.library.book.BookRepositoryImpl;
import org.example.library.model.library.magazine.Magazine;
import org.example.library.model.library.magazine.MagazineRepositoryImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DbLibraryImpl implements Library {

    private final BookRepository bookRepository = BookRepositoryImpl.getInstance();

    private final Map<Class<? extends BaseLibraryModel>, LibraryModelRepository<?>> repositoryMap = Map.ofEntries(
            Map.entry(Book.class, bookRepository),
            Map.entry(Article.class, ArticleRepositoryImpl.getInstance()),
            Map.entry(Magazine.class, MagazineRepositoryImpl.getInstance())
    );


    @Override
    public <T extends BaseLibraryModel> void addItem(T book, Class<T> tClass) {
        try {
            getRepository(tClass).save(book);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public <T extends BaseLibraryModel> void removeItem(T book, Class<T> tClass) {
        try {
            getRepository(tClass).removeOne(book);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public <T extends BaseLibraryModel> T removeItem(Long id, Class<T> tClass) throws ItemNotFoundException {
        try {
            var item = getRepository(tClass).getOne(id);
            getRepository(tClass).removeOne(id);
            return (T) item;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BaseLibraryModel[] search(List<SearchDTO> searchDTOS) {
        return new BaseLibraryModel[0];
    }

    public <T extends BaseLibraryModel> T[] getAll(Class<T> tClass) {
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
    public BaseLibraryModel borrowItem(Long id) throws ItemNotFoundException {
        return null;
    }
    
    @Override
    public <T extends BaseLibraryModel> T getItem(Long id, Class<T> tClass) throws ItemNotFoundException {
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
    public BaseLibraryModel[] getBorrowedItems() {
        return new BaseLibraryModel[0];
    }


    @Override
    public <T extends BaseLibraryModel> T[] addAll(T[] books, Class<T> tClass) {
        try {
            return getRepository(tClass).saveAll(books, tClass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseLibraryModel> LibraryModelRepository<T> getRepository(Class<T> tClass) {
        return (LibraryModelRepository<T>) repositoryMap.get(tClass);
    }
}
