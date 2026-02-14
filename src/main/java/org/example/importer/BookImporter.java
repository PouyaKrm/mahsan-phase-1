package org.example.importer;

import org.example.library.model.BaseLibraryModel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface BookImporter {
    <T extends BaseLibraryModel> T[] getModels(InputStream inputStream, Class<T> clazz, String terminationLine) throws IOException;

    <T extends BaseLibraryModel> void writeToFile(T[] data, Path folderPath, String fileName) throws IOException;

    <T extends BaseLibraryModel> T[] getModels(InputStream inputStream, Class<T> clazz) throws IOException;

    boolean supportsStdIn();
}
