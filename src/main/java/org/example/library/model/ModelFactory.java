package org.example.library.model;

public abstract class ModelFactory<T extends BaseModel> {
    public abstract T createModelFromString(String string);
    public abstract String parseModelToString(T model);

    protected ModelFactory() {

    }
}
