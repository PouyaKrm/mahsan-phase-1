package org.example.library.model.user;

import org.example.exception.ItemNotFoundException;
import org.example.library.model.AbstractModelRepository;
import org.example.library.model.DBFieldMapping;
import org.example.library.model.library.ModelAbstractFactory;

import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public class UserRepositoryImpl extends AbstractModelRepository<User> implements UserRepository {


    private static UserRepositoryImpl instance;
    private static final String TABLE_NAME = "users";
    private static final Map<String, DBFieldMapping> FIELD_MAPPING = Map.ofEntries(
            Map.entry("name", new DBFieldMapping<>(
                            "name",
                            "VARCHAR(40) NOT NULL",
                            (user, value) -> user.setName(value),
                            User::getName, Types.VARCHAR
                    )
            )
    );

    protected UserRepositoryImpl() {
        super(TABLE_NAME, FIELD_MAPPING);
    }


    @Override
    public User[] getAll() throws SQLException {
        return getAll(User.class);
    }

    @Override
    public User[] removeAll() throws SQLException {
        return removeAll(User.class);
    }

    @Override
    public User getOne(Long id) throws SQLException, ItemNotFoundException {
        return getOne(id, User.class);
    }

    public static synchronized UserRepositoryImpl getInstance() {
        if (Objects.nonNull(instance)) {
            return instance;
        }
        instance = new UserRepositoryImpl();
        try {
            instance.createTable();
            return instance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByName(String name) {
        Optional<User> user = Optional.empty();
        try {
            var st = connection.prepareStatement(MessageFormat.format("select * from {0} where name=?", TABLE_NAME));
            var result = st.executeQuery();
            st.setObject(1, name);
            while (result.next()) {
                if (user.isPresent()) {
                    throw new IllegalStateException("query returned more than one result");
                }
                var f = ModelAbstractFactory.getInstance().getDefaultFactory(User.class);
                var u = f.populateFromDB(new User(), result, getFieldMappings().values());
                user = Optional.of(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}
