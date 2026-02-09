package org.example.library.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;

public record DBFieldMapping(String dbFieldName, String properties,
                                                  BiConsumer<BaseModel, String> readFromDB) {

    public String getDBField() {
        return dbFieldName + " " + properties;
    }

    public void setField(BaseModel model, ResultSet resultSet) throws SQLException {
        var val = resultSet.getString(dbFieldName);
        readFromDB.accept(model, val);
    }

}
