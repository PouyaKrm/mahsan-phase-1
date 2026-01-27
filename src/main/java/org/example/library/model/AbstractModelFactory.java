package org.example.library.model;

import org.example.exception.InvalidInputData;

public abstract class AbstractModelFactory<T extends BaseModel> {
    public abstract T createModelFromString(String string) throws InvalidInputData;
    public abstract String parseModelToString(T model);
    public abstract String getDelimeter();

    protected AbstractModelFactory() {

    }
}
