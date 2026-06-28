package io.github.owasp.lab.a04_cryptographic_failures.service;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Safer password hasher.
 * <p>
 * BCrypt is intentionally slow and stores the salt and cost inside the final hash string.
 */
public final class BCryptPasswordHasher {

    private static final int COST = 12;

    private BCryptPasswordHasher() {
    }

    public static String hash(String password) {
        return BCrypt.withDefaults().hashToString(COST, password.toCharArray());
    }
}