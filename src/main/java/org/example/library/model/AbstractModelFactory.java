package org.example.library.model;

import org.example.exception.InvalidInputData;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public abstract class AbstractModelFactory<T extends BaseModel> {
    public abstract T createModelFromString(String string) throws InvalidInputData;

    public abstract String parseModelToString(T model);

    public abstract String getDelimeter();

    public abstract T createFromResultSet(ResultSet resultSet) throws SQLException;

    public T populateFromDB(T model, ResultSet resultSet, Collection<DBFieldMapping> fieldMappings) throws SQLException {
        for (var field : fieldMappings) {
            field.setField(model, resultSet);
        }
        return model;
    }

    protected AbstractModelFactory() {

    }

    public abstract Object createProtoBuffObject(T t);

    public abstract Object createProtoBuffList(T[] items);

    public abstract T[] parseProtoBuffObject(InputStream protoBuffObject) throws IOException;
}
