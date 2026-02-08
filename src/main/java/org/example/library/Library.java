package org.example.library;

import org.example.constansts.ResourceType;
import org.example.exception.ItemNotFoundException;
import org.example.library.dto.SearchDTO;
import org.example.library.model.BaseModel;
import org.example.library.model.article.Article;
import org.example.library.model.book.Book;
import org.example.library.model.magazine.Magazine;

import java.util.List;

public interface Library {
    <T extends BaseModel> void addItem(T book);

    <T extends BaseModel> void removeItem(T book);

    <T extends BaseModel> T removeItem(Long id, ResourceType resourceType);

    BaseModel[] search(List<SearchDTO> searchDTOS);

    BaseModel[] getAll();

    Book[] getAllBooks();

    Article[] getAllArticles();

    Magazine[] getAllMagazines();

    BaseModel borrowItem(Long id) throws ItemNotFoundException;

    BaseModel returnItem(Long id) throws ItemNotFoundException;

    BaseModel[] getBorrowedItems();

    <T extends BaseModel> void addAll(T[] books);
}
