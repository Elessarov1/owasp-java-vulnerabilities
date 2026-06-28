package io.github.owasp.lab.a05_injection.model;

import java.util.List;

public record UserSearchResponse(
        String username,
        String executedSql,
        List<UserAccount> users
) {
}
