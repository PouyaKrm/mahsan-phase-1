package org.example.configs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseConfigs {
    private final String url;
    private final String username;
    private final String password;
    private static DatabaseConfigs instance;

    private static final Pattern PLACEHOLDER =
            Pattern.compile("\\$\\{([^:}]+)(?::([^}]+))?}");

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
            resolveAll(props);
            var url = props.getProperty("db.url");
            var username = props.getProperty("db.username");
            var password = props.getProperty("db.password");
            instance = new DatabaseConfigs(url , username, password);
            return instance;
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize database config", e);
        }


    }

    private static void resolveAll(Properties props) {
        boolean replaced;
        do {
            replaced = false;
            for (String key : props.stringPropertyNames()) {
                String value = props.getProperty(key);
                String resolved = resolveValue(props, value);
                if (!value.equals(resolved)) {
                    props.setProperty(key, resolved);
                    replaced = true;
                }
            }
        } while (replaced);
    }

    private static String resolveValue(Properties props, String value) {
        Matcher matcher = PLACEHOLDER.matcher(value);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String name = matcher.group(1);
            String defaultValue = matcher.group(2);

            String replacement =
                    System.getenv(name) != null
                            ? System.getenv(name)
                            : props.getProperty(name, defaultValue);

            if (replacement == null) {
                throw new IllegalStateException(
                        "Unresolved placeholder: " + name);
            }

            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

}
