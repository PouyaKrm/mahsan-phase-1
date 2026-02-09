package org.example.library.model;

public record DBFieldMapping(String fieldName, String dbFieldName, String properties) {

    public String getDBField() {
        return dbFieldName + " " + properties;
    }

}
