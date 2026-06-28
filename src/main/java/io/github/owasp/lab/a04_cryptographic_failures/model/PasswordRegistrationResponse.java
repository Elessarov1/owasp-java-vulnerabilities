package io.github.owasp.lab.a04_cryptographic_failures.model;

public record PasswordRegistrationResponse(
        String username,
        String algorithm,
        String storedPasswordHash,
        String securityNote
) {
}
