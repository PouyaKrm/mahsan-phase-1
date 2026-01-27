package org.example.utils;

import java.util.Arrays;
import java.util.List;

public class Utils {
    public static <T> T[] listToArray(List<T> list, Class<T> clazz) {
        Object[] result = new Object[list.size()];
        list.toArray(result);
        return Arrays.copyOf(result, result.length,
                (Class<? extends T[]>) java.lang.reflect.Array
                        .newInstance(clazz, 0).getClass());
    }
}
