package org.example.importer;

import org.example.constansts.ResourceType;
import org.example.exception.InvalidInputData;
import org.example.library.model.BaseModel;
import org.example.library.model.AbstractModelFactory;
import org.example.library.model.ModelFactory;
import org.example.library.model.article.ArticleFactory;
import org.example.library.model.book.BookFactory;
import org.example.library.model.magazine.MagazineFactory;

import java.util.*;

public class BookImporterImpl extends AbstractImporter {


    private final ModelFactory factory = ModelFactory.getInstance();

    public <T extends BaseModel> T createModel(ResourceType resourceType, String line, Class<T> clazz) throws InvalidInputData {
        return factory.create(resourceType, line, clazz);
    }

    @Override
    public <T extends BaseModel> T[] getModels(Scanner scanner, ResourceType resourceType, Class<T> clazz) {
        return getModels(scanner, resourceType, clazz, Optional.empty());
    }


}
