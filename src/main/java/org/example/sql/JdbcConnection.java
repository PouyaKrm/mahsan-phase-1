package org.example.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnection {

    private Connection connection;
    private static final JdbcConnection instance = new JdbcConnection();

    private JdbcConnection() {

    }

    private Connection createConnection() throws SQLException {
        if (connection != null) {
            return connection;
        }
        String url = "jdbc:mysql://localhost:5432/phase";
        String username = "admin";
        String password = "admin";

        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public static JdbcConnection getInstance() {
        return instance;
    }
}
