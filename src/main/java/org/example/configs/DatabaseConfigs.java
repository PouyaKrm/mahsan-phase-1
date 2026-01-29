package org.example.configs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class DatabaseConfigs {
    private final String url;
    private final String username;
    private final String password;
    private static DatabaseConfigs instance;

    private DatabaseConfigs(String url, String username, String password)  {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static synchronized DatabaseConfigs getInstance() {

        if(Objects.nonNull(instance)) {
            return instance;
        }

        Properties props = new Properties();

        try (InputStream input =
                     DatabaseConfigs.class.getClassLoader().getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("application.properties not found");
            }
            props.load(input);
            var url = props.getProperty("db.url");
            var username = props.getProperty("db.username");
            var password = props.getProperty("db.password");
            instance = new DatabaseConfigs(url , username, password);
            return instance;
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize database config", e);
        }


    }
}
