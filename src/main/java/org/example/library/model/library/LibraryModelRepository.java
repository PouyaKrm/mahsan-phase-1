package org.example.library.model.library;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.BaseLibraryModel;
import org.example.library.model.ModelRepository;

import java.sql.SQLException;

public interface LibraryModelRepository<T extends BaseLibraryModel> extends ModelRepository<T> {

}
