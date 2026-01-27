package org.example.library.model;

import org.apache.commons.collections4.Factory;
import org.example.constansts.ResourceType;
import org.example.exception.InvalidInputData;
import org.example.library.model.article.Article;
import org.example.library.model.article.ArticleFactory;
import org.example.library.model.book.Book;
import org.example.library.model.book.BookFactory;
import org.example.library.model.magazine.Magazine;
import org.example.library.model.magazine.MagazineFactory;

import java.util.HashMap;
import java.util.Map;

public final class ModelFactory {

    private static final ModelFactory instance = new ModelFactory();

    private ModelFactory() {
        factories.put(Book.class, BookFactory.getFactory());
        factories.put(Article.class, ArticleFactory.getFactory());
        factories.put(Magazine.class, MagazineFactory.getFactory());
    }

    public static ModelFactory getInstance() {
        return instance;
    }

    private final Map<Class<?>, AbstractModelFactory<?>> factories = new HashMap<>();

    public <T extends BaseModel> T create(String line, Class<T> tClass) throws InvalidInputData {
        AbstractModelFactory<?> factory = factories.get(tClass);
        return tClass.cast(factory.createModelFromString(line));
    }

    public <T extends BaseModel> String parseToString(T t) {
        AbstractModelFactory<T> factory = (AbstractModelFactory<T>) factories.get(t.getClass());
        return factory.parseModelToString(t);
    }

    public <T extends BaseModel> AbstractModelFactory<T> getFactory(Class<T> tClass) {
        return (AbstractModelFactory<T>) factories.get(tClass);
    }


}

