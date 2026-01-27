package model.magazine;

import org.example.exception.InvalidInputData;
import org.example.library.model.magazine.MagazineFactory;
import org.junit.Test;

public class MagazineFactoryTest {
    @Test(expected = InvalidInputData.class)
    public void createModelFromString_throws_InvalidInputData_on_invalid_date() throws InvalidInputData {
        var line = "title,author,01-02-20";
        var factory =  MagazineFactory.getFactory();

        factory.createModelFromString(line);
    }
}
