package library;

import org.junit.Before;
import org.junit.BeforeClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Random;


public class BaseDBTest {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }


    @Before
    public void createConnecction() throws SQLException {
        var random  = new Random();
        var rand  = random.nextLong(1000, 10000);
        var url = MessageFormat.format("jdbc:h2:mem:{0}};DB_CLOSE_DELAY=-1", rand);
        connection = DriverManager.getConnection(
                url,
                "sa",
                ""
        );
    }

}
