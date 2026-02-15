package org.example.library;

import org.example.exception.InvalidOperationException;
import org.example.exception.ItemNotFoundException;
import org.example.library.dto.SearchDTO;
import org.example.library.model.BaseLibraryModel;
import org.example.library.model.library.article.Article;
import org.example.library.model.library.book.Book;
import org.example.library.model.library.magazine.Magazine;

import java.util.List;

public interface Library {

    <T extends BaseLibraryModel> void addItem(T book, Class<T> tClass);

    <T extends BaseLibraryModel> void removeItem(T book, Class<T> tClass);

    <T extends BaseLibraryModel> T removeItem(Long id, Class<T> tClass) throws ItemNotFoundException;

    BaseLibraryModel[] search(List<SearchDTO> searchDTOS);

    Book[] getAllBooks();

    Article[] getAllArticles();

    Magazine[] getAllMagazines();

    <T extends BaseLibraryModel> T[] addAll(T[] books, Class<T> tClass);

    BaseLibraryModel borrowItem(Long id) throws ItemNotFoundException, InvalidOperationException;

    <T extends BaseLibraryModel> T getItem(Long id, Class<T> tClass) throws ItemNotFoundException;

    BaseLibraryModel returnItem(Long id) throws ItemNotFoundException;

    BaseLibraryModel[] getBorrowedItems();
}
