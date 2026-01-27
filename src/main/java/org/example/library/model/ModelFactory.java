package org.example.library.model;

import org.example.exception.InvalidInputData;

public abstract class ModelFactory<T extends BaseModel> {
    public abstract T createModelFromString(String string) throws InvalidInputData;
    public abstract String parseModelToString(T model);

    protected ModelFactory() {

    }
}
