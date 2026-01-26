package org.example.importer;

import org.example.constansts.ResourceType;
import org.example.library.model.BaseModel;
import org.example.library.model.ModelFactory;
import org.example.library.model.article.ArticleFactory;
import org.example.library.model.book.BookFactory;
import org.example.library.model.magazine.MagazineFactory;

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
    public <T extends BaseModel> T[] getModels(Scanner scanner, ResourceType resourceType, Class<T> clazz) {
        return getModels(scanner, resourceType, clazz, Optional.empty());
    }

    @Override
    public <T extends BaseModel> T[] getModels(Scanner scanner, ResourceType resourceType, Class<T> clazz, String terminationLine) {
        return getModels(scanner, resourceType, clazz, Optional.of(terminationLine));
    }


    private <T extends BaseModel> T[] getModels(Scanner scanner, ResourceType resourceType, Class<T> clazz, Optional<String> terminationLine) {
        List<Object> books = new ArrayList<>();
        while (scanner.hasNextLine()) {
            try {
                var line = scanner.nextLine();
                if (terminationLine.isPresent() && line.equals(terminationLine.get())) {
                    break;
                }
                var book = createModel(resourceType, line, clazz);
                books.add(book);

            } catch (InvalidParameterException e) {
                e.printStackTrace();
            }

        }

        Object[] result = new Object[books.size()];
        return Arrays.copyOf(result, result.length,
                (Class<? extends T[]>) java.lang.reflect.Array
                        .newInstance(clazz, 0).getClass());
    }
}
