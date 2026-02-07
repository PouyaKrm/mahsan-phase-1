package org.example.library;

import org.example.library.model.BaseModel;
import org.example.library.model.ModelRepository;

import java.sql.SQLException;

public abstract class AbstractModelRepository<T extends BaseModel> implements ModelRepository<T> {

    protected abstract void createTable() throws SQLException;
}
