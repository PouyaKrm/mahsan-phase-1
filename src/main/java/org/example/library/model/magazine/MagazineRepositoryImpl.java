package org.example.library.model.magazine;

import org.example.exception.ItemNotFoundException;
import org.example.library.AbstractModelRepository;

import java.sql.SQLException;
import java.util.Objects;

public class MagazineRepositoryImpl extends AbstractModelRepository<Magazine> implements MagazineRepository {

    private static MagazineRepositoryImpl instance;
    private static final String TABLE_NAME = "magazines";

    protected MagazineRepositoryImpl() {
        super(TABLE_NAME);
    }


    @Override
    public Magazine[] getAll() throws SQLException {
        return getAll(Magazine.class);
    }

    @Override
    public Magazine[] removeAll() throws SQLException {
        return removeAll(Magazine.class);
    }

    @Override
    public Magazine getOne(Long id) throws SQLException, ItemNotFoundException {
        return getOne(id, Magazine.class);
    }

    public static synchronized MagazineRepositoryImpl getInstance() {
        if(Objects.nonNull(instance)) {
            return instance;
        }
        try {
            instance = new MagazineRepositoryImpl();
            instance.createTable();
            return instance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
