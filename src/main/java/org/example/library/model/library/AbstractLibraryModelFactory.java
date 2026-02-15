package org.example.library.model.library;

import org.example.exception.InvalidInputData;
import org.example.library.model.BaseModel;
import org.example.library.model.DefaultModelFactoryImpl;
import org.example.library.model.BaseLibraryModel;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractLibraryModelFactory<T extends BaseModel> extends DefaultModelFactoryImpl<T> {

    public abstract T createModelFromString(String string) throws InvalidInputData;

    public abstract String parseModelToString(T model);

    public abstract String getDelimeter();

    public abstract Object createProtoBuffObject(T t);

    public abstract Object createProtoBuffList(T[] items);

    public abstract T[] parseProtoBuffObject(InputStream protoBuffObject) throws IOException;
}
