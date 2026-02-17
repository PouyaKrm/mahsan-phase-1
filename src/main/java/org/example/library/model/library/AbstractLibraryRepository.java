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

    private static Map<String, DBFieldMapping> getMappings(Map<String, DBFieldMapping<BaseModel>> added, String tableName) {
        Map<String, DBFieldMapping> mappings = new HashMap<>();
        mappings.put("title",  DBFieldMapping.<BaseLibraryModel>builder().tableName(tableName).dbFieldName("title").definition("VARCHAR(100) NOT NULL").fromDB(BaseLibraryModel::setTitle).toDB(BaseLibraryModel::getTitle).dbType( Types.VARCHAR).build());
        mappings.put("author", DBFieldMapping.<BaseLibraryModel>builder().tableName(tableName).dbFieldName("author").definition("VARCHAR(100) NOT NULL").fromDB(BaseLibraryModel::setAuthor).toDB(BaseLibraryModel::getAuthor).dbType(Types.VARCHAR).build());
        mappings.put("content", DBFieldMapping.<BaseLibraryModel>builder().tableName(tableName).dbFieldName("content").definition("TEXT NOT NULL").fromDB(BaseLibraryModel::setContent).toDB(BaseLibraryModel::getContent).dbType(Types.VARCHAR).build());
        mappings.put("pubDate", DBFieldMapping.<BaseLibraryModel>builder().tableName(tableName).dbFieldName("pub_date").definition("INT UNSIGNED NOT NULL").fromDB((BaseLibraryModel model, String value) -> model.setPubDate(parseDate(value))).toDB(BaseLibraryModel::getPubDateEpochDay).dbType(Types.BIGINT).build());
        mappings.put("borrowDate", DBFieldMapping.<BaseLibraryModel>builder().tableName(tableName).dbFieldName("borrow_date").definition("INT UNSIGNED").fromDB((BaseLibraryModel model, String value) -> model.setBorrowDate(parseDate(value))).toDB(BaseLibraryModel::getBorrowDateEpochDay).dbType(Types.BIGINT).build());
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
        super(tableName, getMappings(fieldMappings, tableName));
    }

    public AbstractLibraryRepository(String tableName) {
        super(tableName, getMappings(new HashMap<>(), tableName));
    }

    public AbstractLibraryRepository(String tableName, Connection connection, Map<String, DBFieldMapping<BaseModel>> fieldMappings) {
        super(tableName, connection, getMappings(fieldMappings, tableName));
    }

    public AbstractLibraryRepository(String tableName, Connection connection) {
        super(tableName, connection, getMappings(new HashMap<>(), tableName));
    }
}
