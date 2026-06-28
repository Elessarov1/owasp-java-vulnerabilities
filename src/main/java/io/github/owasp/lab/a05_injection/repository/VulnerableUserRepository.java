package io.github.owasp.lab.a05_injection.repository;

import io.github.owasp.lab.a05_injection.model.UserAccount;
import io.github.owasp.lab.a05_injection.model.UserSearchResponse;

import java.sql.SQLException;
import java.util.ArrayList;

public final class VulnerableUserRepository {

    public UserSearchResponse findByUsername(String username) throws SQLException {
        /*
         * SECURITY BUG:
         *
         * User-controlled input is concatenated directly into the SQL query.
         *
         * Payload example:
         *
         * ' OR '1'='1
         *
         * This changes the WHERE clause and returns all users.
         */
        var sql = """
                SELECT id, username, email, user_role
                FROM lab_users
                WHERE username = '%s'
                ORDER BY id
                """.formatted(username);

        try (var connection = InjectionDatabase.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(sql)) {

            var users = new ArrayList<UserAccount>();

            while (resultSet.next()) {
                users.add(new UserAccount(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("user_role")
                ));
            }

            return new UserSearchResponse(username, sql, users);
        }
    }
}