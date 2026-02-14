package org.example.library.model.magazine;

import org.example.exception.ItemNotFoundException;
import org.example.library.AbstractModelRepository;
import org.example.sql.JdbcConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class MagazineRepositoryImpl extends AbstractModelRepository<Magazine> implements MagazineRepository {

    private static MagazineRepositoryImpl instance;
    private static final String TABLE_NAME = "magazines";

    protected MagazineRepositoryImpl(Connection connection) {
        super(TABLE_NAME, connection);
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

    public static synchronized MagazineRepositoryImpl getInstance(Connection connection) {
        if(Objects.nonNull(instance)) {
            return instance;
        }
        try {
            instance = new MagazineRepositoryImpl(connection);
            instance.createTable();
            return instance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static MagazineRepositoryImpl getInstance() {
        return getInstance(JdbcConnection.getConnection());
    }

}
