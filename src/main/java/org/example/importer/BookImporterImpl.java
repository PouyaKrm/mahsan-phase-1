package org.example.importer;

import org.example.constansts.ResourceType;
import org.example.exception.InvalidInputData;
import org.example.library.model.BaseModel;
import org.example.library.model.AbstractModelFactory;
import org.example.library.model.ModelFactory;
import org.example.library.model.article.ArticleFactory;
import org.example.library.model.book.BookFactory;
import org.example.library.model.magazine.MagazineFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class BookImporterImpl extends AbstractImporter {

    @Override
    public <T extends BaseModel> T[] getModels(InputStream inputStream, ResourceType resourceType, Class<T> clazz) throws IOException {
        return getModels(inputStream, resourceType, clazz, Optional.empty());
    }

    @Override
    public <T extends BaseModel> T[] getModels(InputStream inputStream, ResourceType resourceType, Class<T> clazz, String terminationLine) throws IOException {
        return getModels(inputStream, resourceType, clazz, Optional.of(terminationLine));
    }



}

