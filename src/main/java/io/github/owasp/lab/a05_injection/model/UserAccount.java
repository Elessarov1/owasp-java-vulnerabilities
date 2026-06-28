package io.github.owasp.lab.a05_injection.model;

public record UserAccount(
        long id,
        String username,
        String email,
        String role
) {
}
