package org.example.library.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface ModelFactory<T extends BaseModel> {
    T populateFromDB(T model, ResultSet resultSet, Collection<DBFieldMapping> fieldMappings) throws SQLException;

    T populateFromDB(T model, ResultSet resultSet, Collection<DBFieldMapping> fieldMappings, String tableName) throws SQLException;
}
