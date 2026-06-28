package io.github.owasp.lab.a04_cryptographic_failures.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Vulnerable password hasher.
 * <p>
 * SHA-256 is a fast cryptographic hash function.
 * It is useful for integrity checks, but it is not suitable for password storage.
 */
public final class Sha256PasswordHasher {

    private Sha256PasswordHasher() {
    }

    public static String hash(String password) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            var hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 algorithm is not available", exception);
        }
    }
}