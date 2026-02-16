package org.example.library.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.library.model.BaseModel;
import org.example.library.model.library.book.Book;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class User extends BaseModel {

    private String name;

    @EqualsAndHashCode.Exclude
    List<Book> borrowedBooks = new ArrayList<Book>();
}
