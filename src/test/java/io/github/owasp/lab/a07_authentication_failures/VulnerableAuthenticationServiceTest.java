package io.github.owasp.lab.a07_authentication_failures;

import io.github.owasp.lab.a07_authentication_failures.service.CredentialVerifier;
import io.github.owasp.lab.a07_authentication_failures.service.InvalidCredentialsException;
import io.github.owasp.lab.a07_authentication_failures.vulnerable.VulnerableAuthenticationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VulnerableAuthenticationServiceTest {

    private static final String CLIENT_ID = "192.0.2.10";

    private final CredentialVerifier credentials =
            (username, password) -> username.equals("alice") && password.equals("correct");

    @Test
    void allowsCorrectPasswordAfterUnlimitedFailedAttempts() {
        var service = new VulnerableAuthenticationService(credentials);

        for (var attempt = 0; attempt < 10; attempt++) {
            assertThrows(
                    InvalidCredentialsException.class,
                    () -> service.authenticate("alice", "wrong", CLIENT_ID)
            );
        }

        var response = assertDoesNotThrow(
                () -> service.authenticate("alice", "correct", CLIENT_ID)
        );

        assertTrue(response.authenticated());
    }
}
