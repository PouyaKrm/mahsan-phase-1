package org.example.library.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Types;
import java.util.function.BiConsumer;
import java.util.function.Function;

public record DBFieldMapping(
        String dbFieldName, String properties,
        BiConsumer<BaseModel, String> readFromDB,
        Function<BaseModel, Object> toDB,
        int dbType) {

    public String getDBField() {
        return dbFieldName + " " + properties;
    }

    public void setField(BaseModel model, ResultSet resultSet) throws SQLException {
        var val = resultSet.getString(dbFieldName);
        readFromDB.accept(model, val);
    }

}
