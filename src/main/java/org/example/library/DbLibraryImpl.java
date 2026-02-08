package org.example.library;

import org.example.constansts.ResourceType;
import org.example.exception.ItemNotFoundException;
import org.example.library.dto.SearchDTO;
import org.example.library.model.BaseModel;
import org.example.library.model.article.Article;
import org.example.library.model.book.Book;
import org.example.library.model.book.BookRepository;
import org.example.library.model.book.BookRepositoryImpl;
import org.example.library.model.magazine.Magazine;

import java.util.List;

public class DbLibraryImpl implements Library {

    private final BookRepository bookRepository = BookRepositoryImpl.getInstance();


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

    @Override
    public Book[] getAllBooks() {
        return new Book[0];
    }

    @Override
    public Article[] getAllArticles() {
        return new Article[0];
    }

    @Override
    public Magazine[] getAllMagazines() {
        return new Magazine[0];
    }

    @Override
    public BaseModel borrowItem(Long id) throws ItemNotFoundException {
        return null;
    }

    @Override
    public BaseModel returnItem(Long id) throws ItemNotFoundException {
        return null;
    }

    @Override
    public BaseModel[] getBorrowedItems() {
        return new BaseModel[0];
    }

    @Override
    public <T extends BaseModel> void addAll(T[] books) {

    }
}
