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

    public static String formatFileExtension(String fileName, String extension) {
        var index = fileName.lastIndexOf(".");
        if (index == -1 && extension.isEmpty()) {
            return fileName;
        } else if (index == -1) {
            throw new IllegalArgumentException("invalid file extension");
        }
        return fileName.substring(0, index) + extension;
    }
}
