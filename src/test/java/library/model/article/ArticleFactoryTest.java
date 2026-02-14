package library.model.article;

import org.example.exception.InvalidInputData;
import org.example.library.model.library.article.ArticleFactory;
import org.junit.Test;

public class ArticleFactoryTest {

    @Test(expected = InvalidInputData.class)
    public void createModelFromString_throws_InvalidInputData_on_invalid_date() throws InvalidInputData {
        var line = "title,author,01-02-20,content";
        var factory =  ArticleFactory.getFactory();

        factory.createModelFromString(line);
    }

}
