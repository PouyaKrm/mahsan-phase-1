package org.example.library.model;

import org.example.exception.InvalidInputData;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractModelFactory<T extends BaseModel> {
    public abstract T createModelFromString(String string) throws InvalidInputData;
    public abstract String parseModelToString(T model);
    public abstract String getDelimeter();
    public abstract T createFromResultSet(ResultSet resultSet) throws SQLException;

    protected AbstractModelFactory() {

    }
}
