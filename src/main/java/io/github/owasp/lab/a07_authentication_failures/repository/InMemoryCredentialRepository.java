package io.github.owasp.lab.a07_authentication_failures.repository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.github.owasp.lab.a07_authentication_failures.service.CredentialVerifier;

import java.util.Locale;
import java.util.Map;

public final class InMemoryCredentialRepository implements CredentialVerifier {

    public static final String DEMO_USERNAME = "alice";
    public static final String DEMO_PASSWORD = "Summer2026!";

    private static final int BCRYPT_COST = 12;

    private final Map<String, String> passwordHashes;

    public InMemoryCredentialRepository() {
        passwordHashes = Map.of(
                DEMO_USERNAME,
                BCrypt.withDefaults().hashToString(BCRYPT_COST, DEMO_PASSWORD.toCharArray())
        );
    }

    @Override
    public boolean matches(String username, String password) {
        var normalizedUsername = username.toLowerCase(Locale.ROOT);
        var storedHash = passwordHashes.get(normalizedUsername);

        /*
         * Verify unknown users against a real hash as well. This keeps the
         * expensive BCrypt operation in both paths and reduces username
         * enumeration through response timing.
         */
        var hashToVerify = storedHash != null
                ? storedHash
                : passwordHashes.get(DEMO_USERNAME);

        var passwordMatches = BCrypt.verifyer()
                .verify(password.toCharArray(), hashToVerify)
                .verified;

        return storedHash != null && passwordMatches;
    }
}
