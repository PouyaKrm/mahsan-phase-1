package org.example.importer;

import org.example.constansts.ResourceType;
import org.example.library.model.BaseModel;
import org.example.library.model.ModelFactory;
import org.example.library.model.article.ArticleFactory;
import org.example.library.model.book.BookFactory;
import org.example.library.model.magazine.MagazineFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.*;

public class BookImporterImpl implements BookImporter {


    private final Map<ResourceType, ModelFactory<?>> factories = Map.ofEntries(
            Map.entry(ResourceType.BOOK, BookFactory.getFactory()),
            Map.entry(ResourceType.ARTICLE, ArticleFactory.getFactory()),
            Map.entry(ResourceType.MAGAZINE, MagazineFactory.getFactory())
    );

    public <T extends BaseModel> T createModel(ResourceType resourceType, String line, Class<T> clazz) {
        var factory = factories.get(resourceType);
        return clazz.cast(factory.createModelFromString(line));
    }

    @Override
    public <T extends BaseModel> T[] getModels(InputStream inputStream, ResourceType resourceType, Class<T> clazz) throws IOException {
        return getModels(inputStream, resourceType, clazz, Optional.empty());
    }

    @Override
    public <T extends BaseModel> T[] getModels(InputStream inputStream, ResourceType resourceType, Class<T> clazz, String terminationLine) throws IOException {
        return getModels(inputStream, resourceType, clazz, Optional.of(terminationLine));
    }


    private <T extends BaseModel> T[] getModels(InputStream inputStream, ResourceType resourceType, Class<T> clazz, Optional<String> terminationLine) throws IOException {
        List<Object> books = new ArrayList<>();
        try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line =  reader.readLine()) != null) {
                if (terminationLine.isPresent() && line.equals(terminationLine.get())) {
                    break;
                }
                var book = createModel(resourceType, line, clazz);
                books.add(book);
            }

            Object[] result = new Object[books.size()];
            books.toArray(result);
            return Arrays.copyOf(result, result.length,
                    (Class<? extends T[]>) java.lang.reflect.Array
                            .newInstance(clazz, 0).getClass());
        }
    }
}
