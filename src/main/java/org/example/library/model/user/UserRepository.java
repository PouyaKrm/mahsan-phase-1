package org.example.library.model.user;

import org.example.library.model.ModelRepository;

import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository extends ModelRepository<User> {
    Optional<User> findByName(String name);

    User getDefaultUser() throws SQLException;
}
