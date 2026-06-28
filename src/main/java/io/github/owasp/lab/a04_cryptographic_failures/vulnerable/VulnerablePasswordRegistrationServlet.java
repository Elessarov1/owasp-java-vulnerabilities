package io.github.owasp.lab.a04_cryptographic_failures.vulnerable;

import io.github.owasp.lab.a04_cryptographic_failures.model.PasswordRegistrationResponse;
import io.github.owasp.lab.a04_cryptographic_failures.service.Sha256PasswordHasher;
import io.github.owasp.lab.common.http.JsonResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Vulnerable example: Cryptographic Failures.
 * <p>
 * The application stores a password hash created with SHA-256.
 * This is insecure because SHA-256 is too fast for password storage and does not
 * provide a work factor. In this example, the code also does not use a unique salt.
 */
public final class VulnerablePasswordRegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var username = request.getParameter("username");
        var password = request.getParameter("password");

        if (username == null || username.isBlank()) {
            JsonResponse.error(response, HttpServletResponse.SC_BAD_REQUEST, "username is required");
            return;
        }

        if (password == null || password.isBlank()) {
            JsonResponse.error(response, HttpServletResponse.SC_BAD_REQUEST, "password is required");
            return;
        }

        /*
         * SECURITY BUG:
         *
         * SHA-256 is a fast hash function.
         *
         * Fast hashes are not appropriate for password storage because attackers
         * can test many password guesses per second after a database leak.
         *
         * This example also does not use a unique per-password salt, so the same
         * password always produces the same stored hash.
         */
        var storedPasswordHash = Sha256PasswordHasher.hash(password);

        JsonResponse.ok(response, new PasswordRegistrationResponse(
                username,
                "SHA-256",
                storedPasswordHash,
                "Vulnerable: fast unsalted password hash. Same password produces the same hash."
        ));
    }
}