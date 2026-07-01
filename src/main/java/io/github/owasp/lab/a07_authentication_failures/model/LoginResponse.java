package io.github.owasp.lab.a07_authentication_failures.model;

public record LoginResponse(
        boolean authenticated,
        String username,
        String message
) {
}
