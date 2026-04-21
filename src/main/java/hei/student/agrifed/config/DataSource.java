package hei.student.agrifed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DataSource {
    private final String url = System.getenv("JDBC_URL");
    private final String user  = System.getenv("JDBC_USER");
    private final String password = System.getenv("JDBC_PASSWORD");

    @Bean
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    url, user, password
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
