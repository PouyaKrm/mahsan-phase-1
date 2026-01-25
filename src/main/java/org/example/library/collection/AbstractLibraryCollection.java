package org.example.library.collection;

import org.example.library.model.BaseModel;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractLibraryCollection<T extends BaseModel> implements LibraryCollection<T> {

    protected HashMap<String, String> getFieldNameValues(T item) throws IllegalAccessException {
        Class<?> clazz = item.getClass();
        Field[] fields = clazz.getDeclaredFields();
        final HashMap<String,String> fieldNameValues = new HashMap<>();
        for (Field field : fields) {
            if (field.getType() == String.class) {
                fieldNameValues.put(field.getName(), (String) field.get(item));
            }
        }
        return fieldNameValues;
    };
}
