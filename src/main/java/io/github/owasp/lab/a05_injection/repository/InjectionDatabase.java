package io.github.owasp.lab.a05_injection.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class InjectionDatabase {

    private static final String JDBC_URL = "jdbc:h2:mem:owasp_a05;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static boolean initialized;

    private InjectionDatabase() {
    }

    public static Connection getConnection() throws SQLException {
        loadDriver();

        var connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        initializeIfNeeded(connection);

        return connection;
    }

    private static void loadDriver() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("H2 JDBC driver is not available", exception);
        }
    }

    private static synchronized void initializeIfNeeded(Connection connection) throws SQLException {
        if (initialized) {
            return;
        }

        try (var statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS lab_users (
                        id BIGINT PRIMARY KEY,
                        username VARCHAR(100) NOT NULL UNIQUE,
                        email VARCHAR(255) NOT NULL,
                        user_role VARCHAR(50) NOT NULL
                    )
                    """);

            statement.execute("DELETE FROM lab_users");

            statement.execute("""
                    INSERT INTO lab_users (id, username, email, user_role)
                    VALUES
                        (1, 'alice', 'alice@example.com', 'USER'),
                        (2, 'bob', 'bob@example.com', 'USER'),
                        (3, 'admin', 'admin@example.com', 'ADMIN')
                    """);
        }

        initialized = true;
    }
}