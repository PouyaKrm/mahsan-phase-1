package org.example.library.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Function;

public record DBFieldMapping<T extends BaseModel>(
        String dbFieldName, String properties,
        BiConsumer<T, String> readFromDB,
        Function<T, Object> toDB,
        int dbType) {

    public String getDBField() {
        return dbFieldName + " " + properties;
    }

    public String getColumnLabel(String tableName) {
        return tableName + "_" + dbFieldName;
    }

    public void setField(T model, ResultSet resultSet) throws SQLException {
        var val = resultSet.getString(dbFieldName);
        readFromDB.accept(model, val);
    }

    public void setField(T model, ResultSet resultSet, String tableName) throws SQLException {
        var val = resultSet.getString(getColumnLabel(tableName));
        readFromDB.accept(model, val);
    }

}
