package io.github.owasp.lab.a07_authentication_failures.fixed;

import io.github.owasp.lab.a07_authentication_failures.service.TooManyAttemptsException;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class LoginAttemptLimiter {

    public static final int MAX_FAILURES = 3;
    public static final Duration BLOCK_DURATION = Duration.ofSeconds(30);

    private final Clock clock;
    private final Map<String, AttemptState> attempts = new HashMap<>();

    public LoginAttemptLimiter() {
        this(Clock.systemUTC());
    }

    public LoginAttemptLimiter(Clock clock) {
        this.clock = clock;
    }

    public synchronized void checkAllowed(String username, String clientId) {
        var accountRetryAfter = retryAfter(accountKey(username));
        var clientRetryAfter = retryAfter(clientKey(clientId));
        var retryAfterSeconds = Math.max(accountRetryAfter, clientRetryAfter);

        if (retryAfterSeconds > 0) {
            throw new TooManyAttemptsException(retryAfterSeconds);
        }
    }

    public synchronized void recordFailure(String username, String clientId) {
        recordFailure(accountKey(username));
        recordFailure(clientKey(clientId));
    }

    public synchronized void recordSuccess(String username, String clientId) {
        attempts.remove(accountKey(username));
        attempts.remove(clientKey(clientId));
    }

    public synchronized void reset() {
        attempts.clear();
    }

    private void recordFailure(String key) {
        var previous = attempts.getOrDefault(key, new AttemptState(0, null));
        var failures = previous.failures() + 1;
        var blockedUntil = failures >= MAX_FAILURES
                ? clock.instant().plus(BLOCK_DURATION)
                : null;

        attempts.put(key, new AttemptState(failures, blockedUntil));
    }

    private long retryAfter(String key) {
        var state = attempts.get(key);

        if (state == null || state.blockedUntil() == null) {
            return 0;
        }

        var now = clock.instant();

        if (!state.blockedUntil().isAfter(now)) {
            attempts.remove(key);
            return 0;
        }

        var remainingMillis = Duration.between(now, state.blockedUntil()).toMillis();
        return Math.max(1, (remainingMillis + 999) / 1000);
    }

    private String accountKey(String username) {
        return "account:" + username.toLowerCase(Locale.ROOT);
    }

    private String clientKey(String clientId) {
        return "client:" + clientId;
    }

    private record AttemptState(int failures, Instant blockedUntil) {
    }
}
