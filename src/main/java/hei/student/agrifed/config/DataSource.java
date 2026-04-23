package hei.student.agrifed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DataSource {
    private final String URL = System.getenv("JDBC_URL");
    private final String USER = System.getenv("JDBC_USER");
    private final String PASSWORD = System.getenv("JDBC_PASSWORD");

    @Bean
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    URL, USER, PASSWORD
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
