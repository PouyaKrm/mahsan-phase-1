package org.example.importer;

import org.example.constansts.ResourceType;
import org.example.exception.InvalidInputData;
import org.example.library.model.BaseModel;
import org.example.library.model.ModelFactory;

import java.security.InvalidParameterException;
import java.util.*;

public abstract class AbstractImporter implements BookImporter {


    private final ModelFactory factory = ModelFactory.getInstance();

    @Override
    public <T extends BaseModel> T[] getModels(Scanner scanner, ResourceType resourceType, Class<T> clazz, String terminationLine) {
        return getModels(scanner, resourceType, clazz, Optional.of(terminationLine));
    }


    protected  <T extends BaseModel> T[] getModels(Scanner scanner, ResourceType resourceType, Class<T> clazz, Optional<String> terminationLine) {
        List<Object> books = new ArrayList<>();
        while (scanner.hasNextLine()) {
            try {
                var line = scanner.nextLine();
                if (terminationLine.isPresent() && line.equals(terminationLine.get())) {
                    break;
                }
                var book = factory.create(resourceType, line, clazz);
                books.add(book);

            } catch (InvalidParameterException e) {
                e.printStackTrace();
            } catch (InvalidInputData e) {
                System.err.println(e.getMessage());
            }

        }

        Object[] result = new Object[books.size()];
        books.toArray(result);
        return Arrays.copyOf(result, result.length,
                (Class<? extends T[]>) java.lang.reflect.Array
                        .newInstance(clazz, 0).getClass());
    }
}
