package org.example.library.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.library.Displayable;
import org.example.library.model.BaseModel;
import org.example.library.model.library.book.Book;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class User extends BaseModel implements Displayable {

    private String name;

    @EqualsAndHashCode.Exclude
    List<Book> borrowedBooks = new ArrayList<Book>();

    @Override
    public void display() {
        System.out.println(MessageFormat.format("{0} - {1}", getId(), getName()));
    }
}
