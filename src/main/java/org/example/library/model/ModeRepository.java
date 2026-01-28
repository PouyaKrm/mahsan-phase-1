package org.example.library.model;

import java.sql.SQLException;

public interface ModeRepository<T extends BaseModel> {
    T[] getAll() throws SQLException;
    T addOne(T model);
    void removeOne(T model);
    T getOne(int id);
}
