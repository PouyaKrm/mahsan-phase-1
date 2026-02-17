package org.example.library.model;

import org.example.exception.ItemNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ModelRepository<T extends BaseModel> {
    T[] getAll() throws SQLException;
    T[] removeAll() throws SQLException;
    T save(T model) throws SQLException;
    boolean removeOne(T model) throws SQLException;
    boolean removeOne(Long id) throws SQLException;
    T getOne(Long id) throws SQLException, ItemNotFoundException;
    T[] saveAll(T[] models, Class<T> tClass) throws SQLException;

    List<DBFieldMapping> getFieldMappings();

    Map<String, DBFieldMapping> getFieldMappingMap();

    List<DBFieldMapping> nonIdFieldMappings();

    String getTableName();

    String getAllColumnsSelectLabel();
}
