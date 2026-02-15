package org.example.library.model.library;

import org.example.constansts.ResourceType;
import org.example.exception.InvalidInputData;
import org.example.library.model.BaseModel;
import org.example.library.model.DefaultModelFactoryImpl;
import org.example.library.model.ModelFactory;
import org.example.library.model.library.article.Article;
import org.example.library.model.library.article.ArticleFactoryLibrary;
import org.example.library.model.library.book.Book;
import org.example.library.model.library.book.BookFactoryLibrary;
import org.example.library.model.library.magazine.Magazine;
import org.example.library.model.library.magazine.MagazineFactoryLibrary;

import java.util.HashMap;
import java.util.Map;

public final class ModelAbstractFactory {

    private static final ModelAbstractFactory instance = new ModelAbstractFactory();

    private ModelAbstractFactory() {
//        factories.put(Book.class, BookFactoryLibrary.getFactory());
//        factories.put(Article.class, ArticleFactoryLibrary.getFactory());
//        factories.put(Magazine.class, MagazineFactoryLibrary.getFactory());
    }

    public static ModelAbstractFactory getInstance() {
        return instance;
    }

    private final Map<Class<?>, ModelFactory<?>> factories = new HashMap<>();

    public <T extends BaseModel> ModelFactory<T> getFactory(Class<T> tClass) {
        return (ModelFactory<T>) factories.get(tClass);
    }

    public <T extends BaseModel> ModelFactory<T> getDefaultFactory(Class<T> tClass) {
        return new DefaultModelFactoryImpl<>();
    }

    public void registerFaccotry(ModelFactory<?> factory, Class<?> tClass) {
        factories.put(tClass, factory);
    }

    public <T extends BaseModel> ModelFactory<T> getFactory(ResourceType resourceType) {
        return switch (resourceType) {
            case BOOK -> (ModelFactory<T>) getFactory(Book.class);
            case ARTICLE -> (ModelFactory<T>) getFactory(Article.class);
            case MAGAZINE -> (ModelFactory<T>) getFactory(Magazine.class);
            default -> new DefaultModelFactoryImpl<>();
        };
    }


}

