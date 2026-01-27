package org.example.importer;

import org.example.constansts.ResourceType;
import org.example.exception.InvalidInputData;
import org.example.library.model.BaseModel;
import org.example.library.model.ModelFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BookImporterImpl implements BookImporter {

    private final ModelFactory factory = ModelFactory.getInstance();

    @Override
    public <T extends BaseModel> T[] getModels(InputStream inputStream, ResourceType resourceType, Class<T> clazz) throws IOException {
        return getModels(inputStream, resourceType, clazz, Optional.empty());
    }

    @Override
    public <T extends BaseModel> T[] getModels(InputStream inputStream, ResourceType resourceType, Class<T> clazz, String terminationLine) throws IOException {
        return getModels(inputStream, resourceType, clazz, Optional.of(terminationLine));
    }

    @Override
    public boolean supportsStdIn() {
        return true;
    }


    private   <T extends BaseModel> T[] getModels(InputStream inputStream, ResourceType resourceType, Class<T> clazz, Optional<String> terminationLine) throws IOException {
        List<Object> books = new ArrayList<>();
        try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (terminationLine.isPresent() && line.equals(terminationLine.get())) {
                    break;
                }
                var book = factory.create(resourceType, line, clazz);
                books.add(book);

            }

        } catch (InvalidInputData e) {
            throw new RuntimeException(e);
        }

        Object[] result = new Object[books.size()];
        books.toArray(result);
        return Arrays.copyOf(result, result.length,
                (Class<? extends T[]>) java.lang.reflect.Array
                        .newInstance(clazz, 0).getClass());
    }

}

