package org.example.library.model.library;

import org.example.library.model.AbstractModelRepository;
import org.example.library.model.BaseModel;
import org.example.library.model.BaseLibraryModel;
import org.example.library.model.DBFieldMapping;
import org.example.utils.DateUtils;

import java.sql.Connection;
import java.sql.Types;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractLibraryRepository<T extends BaseLibraryModel> extends AbstractModelRepository<T> {

    private static Map<String, DBFieldMapping> getMappings(Map<String, DBFieldMapping<BaseModel>> added) {
        Map<String, DBFieldMapping> mappings = new HashMap<>();
        mappings.put("title", new DBFieldMapping<BaseLibraryModel>("title", "VARCHAR(100) NOT NULL", BaseLibraryModel::setTitle, BaseLibraryModel::getTitle, Types.VARCHAR));
        mappings.put("author", new DBFieldMapping<>("author", "VARCHAR(100) NOT NULL", BaseLibraryModel::setAuthor, BaseLibraryModel::getAuthor, Types.VARCHAR));
        mappings.put("content", new DBFieldMapping<>("content", "TEXT NOT NULL", BaseLibraryModel::setContent, BaseLibraryModel::getContent, Types.VARCHAR));
        mappings.put("pubDate", new DBFieldMapping<>("pub_date", "INT UNSIGNED NOT NULL", (BaseLibraryModel model, String value) -> model.setPubDate(parseDate(value)), BaseLibraryModel::getPubDateEpochDay, Types.BIGINT));
        mappings.put("borrowDate", new DBFieldMapping<>("borrow_date", "INT UNSIGNED", (BaseLibraryModel model, String value) -> model.setBorrowDate(parseDate(value)), BaseLibraryModel::getBorrowDateEpochDay, Types.BIGINT));
        for (var mapping : added.entrySet()) {
            if (mappings.containsKey(mapping.getKey())) {
                mappings.replace(mapping.getKey(), mapping.getValue());
            } else {
                mappings.put(mapping.getKey(), mapping.getValue());
            }
        }
        return mappings;
    }

    private static LocalDate parseDate(String date) {
        if (Objects.isNull(date)) {
            return null;
        }
        return DateUtils.createDateEpochDay(Integer.parseInt(date));
    }

    public AbstractLibraryRepository(String tableName, HashMap<String, DBFieldMapping<BaseModel>> fieldMappings) {
        super(tableName, getMappings(fieldMappings));
    }

    public AbstractLibraryRepository(String tableName) {
        super(tableName, getMappings(new HashMap<>()));
    }

    public AbstractLibraryRepository(String tableName, Connection connection, Map<String, DBFieldMapping<BaseModel>> fieldMappings) {
        super(tableName, connection, getMappings(fieldMappings));
    }

    public AbstractLibraryRepository(String tableName, Connection connection) {
        super(tableName, connection, getMappings(new HashMap<>()));
    }
}
