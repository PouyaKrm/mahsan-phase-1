package org.example;

import org.example.cli.LibraryCLI;
import org.example.library.model.user.User;
import org.example.library.model.user.UserRepository;
import org.example.library.model.user.UserRepositoryImpl;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class Main {

    private static final UserRepository userRepository = UserRepositoryImpl.getInstance();

    public static void main(String[] args) throws Exception {
        createUser();
        var cli = new LibraryCLI();
        cli.start();
    }

    private static void createUser() throws SQLException {
        var u = new User();
        u.setName("username");
        userRepository.save(u);
    }


}