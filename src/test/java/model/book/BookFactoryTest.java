package model.book;

import org.example.exception.InvalidInputData;
import org.example.library.model.book.BookFactory;
import org.junit.Test;

public class BookFactoryTest {
    @Test(expected = InvalidInputData.class)
    public void createModelFromString_throws_InvalidInputData_on_invalid_date() throws InvalidInputData {
        var line = "title,author,01-02-20,content,EXIST";
        var factory =  BookFactory.getFactory();

        factory.createModelFromString(line);
    }
}
