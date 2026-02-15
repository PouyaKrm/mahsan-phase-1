package org.example.library.model.user;

import lombok.Data;
import org.example.library.model.BaseModel;
import org.example.library.model.library.book.Book;

import java.util.ArrayList;
import java.util.List;

@Data
public class User extends BaseModel {

    private String name;

    List<Book> borrowedBooks = new ArrayList<Book>();
}
