package org.example.library.model.user;

import org.example.library.model.ModelRepository;

import java.util.Optional;

public interface UserRepository extends ModelRepository<User> {
    Optional<User> findByName(String name);
}
