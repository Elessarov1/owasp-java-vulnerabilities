package io.github.owasp.lab.a05_injection.fixed;

import io.github.owasp.lab.a05_injection.model.UserAccount;
import io.github.owasp.lab.a05_injection.model.UserSearchResponse;
import io.github.owasp.lab.a05_injection.repository.InjectionDatabase;
import io.github.owasp.lab.a05_injection.service.UserSearchService;

import java.sql.SQLException;
import java.util.ArrayList;

public final class FixedUserSearchService implements UserSearchService {

    @Override
    public UserSearchResponse findByUsername(String username) throws SQLException {
        /*
         * FIX:
         *
         * The SQL query uses a placeholder.
         * The user-controlled value is bound as data through PreparedStatement.
         *
         * Even if the input contains SQL syntax, it is treated as a plain string,
         * not as part of the query.
         */
        var sql = """
                SELECT id, username, email, user_role
                FROM lab_users
                WHERE username = ?
                ORDER BY id
                """;

        try (var connection = InjectionDatabase.getConnection();
             var statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (var resultSet = statement.executeQuery()) {
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
}
