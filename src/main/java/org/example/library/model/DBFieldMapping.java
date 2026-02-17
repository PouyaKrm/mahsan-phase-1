package org.example.library.model;

import lombok.Builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Builder
public class DBFieldMapping<T extends BaseModel> {

    private String dbFieldName;
    private String definition;
    private BiConsumer<T, String> fromDB;
    private Function<T, Object> toDB;
    private String tableName;
    private int dbType;

    public String getDBField() {
        return dbFieldName + " " + definition;
    }

    public String getColumnLabel() {
        return tableName + "_" + dbFieldName;
    }

    public String getDbFieldNameDotted() {
        return tableName + "." + dbFieldName;
    }

    public void setField(T model, ResultSet resultSet) throws SQLException {
        var val = resultSet.getString(dbFieldName);
        fromDB.accept(model, val);
    }

    public String dbFieldName() {
        return dbFieldName;
    }

    public BiConsumer<T, String> fromDB() {
        return fromDB;
    }

    public Function<T, Object> toDB() {
        return toDB;
    }

    public int dbType() {
        return dbType;
    }

}
