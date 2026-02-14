package org.example.importer;

import org.example.constansts.ResourceType;
import org.example.library.model.BaseModel;
import org.example.library.model.ModelFactory;
import org.example.library.v1.ArticleList;
import org.example.library.v1.BookList;
import org.example.library.v1.MagazineList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public class ProtoBuggerImporterImpl implements BookImporter {

    private final ModelFactory factory = ModelFactory.getInstance();

    @Override
    public <T extends BaseModel> T[] getModels(InputStream inputStream, Class<T> clazz, String terminationLine) throws IOException {
        var f = factory.getFactory(clazz);
        return f.parseProtoBuffObject(inputStream);
    }

    @Override
    public <T extends BaseModel> void writeToFile(T[] data, Path folderPath, String fileName) throws IOException {
        var resource = data[0].resourceType();
        try (OutputStream outputStream = new FileOutputStream(folderPath.resolve(fileName).toFile())) {
            write(data, resource, outputStream);
        }
    }

    @Override
    public <T extends BaseModel> T[] getModels(InputStream inputStream, Class<T> clazz) throws IOException {
        return null;
    }

    @Override
    public boolean supportsStdIn() {
        return false;
    }

    private <T extends BaseModel> void write(T[] array, ResourceType resourceType, OutputStream outputStream) throws IOException {
        var f = factory.getFactory(resourceType);
        var bufferList = f.createProtoBuffList(array);
        switch (resourceType) {
            case BOOK -> ((BookList) bufferList).writeTo(outputStream);
            case ARTICLE -> ((ArticleList) bufferList).writeTo(outputStream);
            case MAGAZINE -> ((MagazineList) bufferList).writeTo(outputStream);
        }
    }
}
