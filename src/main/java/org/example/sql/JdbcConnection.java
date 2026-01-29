package org.example.sql;

import org.example.configs.DatabaseConfigs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class JdbcConnection {

    private static Connection connection;


    private JdbcConnection() {

    }


    public static synchronized Connection getConnection() {
        if (Objects.nonNull(connection)) {
            return connection;
        }
        var configs = DatabaseConfigs.getInstance();

        try {
            connection = DriverManager.getConnection(configs.getUrl(), configs.getUsername(), configs.getPassword());
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
