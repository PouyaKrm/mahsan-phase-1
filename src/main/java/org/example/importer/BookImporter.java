package org.example.importer;

import org.example.constansts.ResourceType;
import org.example.library.model.BaseModel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface BookImporter {
    <T extends BaseModel> T[] getModels(InputStream inputStream, ResourceType resourceType, Class<T> clazz) throws IOException;

    <T extends BaseModel> T[] getModels(InputStream inputStream, ResourceType resourceType, Class<T> clazz, String terminationLine) throws IOException;

    <T extends BaseModel> void writeToFile(T[] data, Path filePath) throws IOException;

    Object[] getModels(InputStream inputStream) throws IOException;


    boolean supportsStdIn();
}
