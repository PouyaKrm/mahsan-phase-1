package org.example.library.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class DefaultModelFactoryImpl<T extends BaseModel> implements ModelFactory<T> {
    @Override
    public T populateFromDB(T model, ResultSet resultSet, Collection<DBFieldMapping> fieldMappings) throws SQLException {
        for (var field : fieldMappings) {
            field.setField(model, resultSet);
        }
        return model;
    }
}
