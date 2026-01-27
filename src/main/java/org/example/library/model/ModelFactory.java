package org.example.library.model;

import org.example.constansts.ResourceType;
import org.example.exception.InvalidInputData;
import org.example.library.model.article.ArticleFactory;
import org.example.library.model.book.BookFactory;
import org.example.library.model.magazine.MagazineFactory;

import java.util.Map;

public final class ModelFactory {

    private static final ModelFactory instance = new ModelFactory();

    private ModelFactory() {

    }

    public static ModelFactory getInstance() {
        return instance;
    }

    private final Map<ResourceType, AbstractModelFactory<?>> factories = Map.ofEntries(
            Map.entry(ResourceType.BOOK, BookFactory.getFactory()),
            Map.entry(ResourceType.ARTICLE, ArticleFactory.getFactory()),
            Map.entry(ResourceType.MAGAZINE, MagazineFactory.getFactory())
    );

    public  <T extends BaseModel> T create(ResourceType resourceType, String line, Class<T> tClass) throws InvalidInputData {
        AbstractModelFactory<?> factory = factories.get(resourceType);
        return tClass.cast(factory.createModelFromString(line));
    }
}
