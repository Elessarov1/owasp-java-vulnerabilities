package io.github.owasp.lab.a07_authentication_failures;

import io.github.owasp.lab.a07_authentication_failures.fixed.FixedAuthenticationService;
import io.github.owasp.lab.a07_authentication_failures.fixed.LoginAttemptLimiter;
import io.github.owasp.lab.a07_authentication_failures.service.CredentialVerifier;
import io.github.owasp.lab.a07_authentication_failures.service.InvalidCredentialsException;
import io.github.owasp.lab.a07_authentication_failures.service.TooManyAttemptsException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FixedAuthenticationServiceTest {

    private static final String CLIENT_ID = "192.0.2.10";

    private final CredentialVerifier credentials =
            (username, password) -> username.equals("alice") && password.equals("correct");

    @Test
    void blocksAuthenticationAfterThreeFailuresAndAllowsItAfterCooldown() {
        var clock = new MutableClock(Instant.parse("2026-07-01T10:00:00Z"));
        var service = service(clock);

        failLogin(service, "alice", CLIENT_ID);
        failLogin(service, "alice", CLIENT_ID);
        failLogin(service, "alice", CLIENT_ID);

        var exception = assertThrows(
                TooManyAttemptsException.class,
                () -> service.authenticate("alice", "correct", CLIENT_ID)
        );

        assertEquals(30, exception.retryAfterSeconds());

        clock.advance(Duration.ofSeconds(31));

        var response = assertDoesNotThrow(
                () -> service.authenticate("alice", "correct", CLIENT_ID)
        );
        assertTrue(response.authenticated());
    }

    @Test
    void successfulAuthenticationClearsPreviousFailures() {
        var service = service(Clock.fixed(
                Instant.parse("2026-07-01T10:00:00Z"),
                ZoneOffset.UTC
        ));

        failLogin(service, "alice", CLIENT_ID);
        failLogin(service, "alice", CLIENT_ID);
        assertDoesNotThrow(() -> service.authenticate("alice", "correct", CLIENT_ID));

        failLogin(service, "alice", CLIENT_ID);
        failLogin(service, "alice", CLIENT_ID);
        assertDoesNotThrow(() -> service.authenticate("alice", "correct", CLIENT_ID));
    }

    @Test
    void limitsRepeatedAttemptsFromOneClientAcrossUsernames() {
        var service = service(Clock.fixed(
                Instant.parse("2026-07-01T10:00:00Z"),
                ZoneOffset.UTC
        ));

        failLogin(service, "alice", CLIENT_ID);
        failLogin(service, "bob", CLIENT_ID);
        failLogin(service, "carol", CLIENT_ID);

        assertThrows(
                TooManyAttemptsException.class,
                () -> service.authenticate("alice", "correct", CLIENT_ID)
        );
    }

    private FixedAuthenticationService service(Clock clock) {
        return new FixedAuthenticationService(
                credentials,
                new LoginAttemptLimiter(clock)
        );
    }

    private void failLogin(
            FixedAuthenticationService service,
            String username,
            String clientId
    ) {
        assertThrows(
                InvalidCredentialsException.class,
                () -> service.authenticate(username, "wrong", clientId)
        );
    }

    private static final class MutableClock extends Clock {

        private Instant instant;

        private MutableClock(Instant instant) {
            this.instant = instant;
        }

        private void advance(Duration duration) {
            instant = instant.plus(duration);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
