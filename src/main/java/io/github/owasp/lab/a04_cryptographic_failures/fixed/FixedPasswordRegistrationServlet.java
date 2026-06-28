package io.github.owasp.lab.a04_cryptographic_failures.fixed;

import io.github.owasp.lab.a04_cryptographic_failures.model.PasswordRegistrationResponse;
import io.github.owasp.lab.a04_cryptographic_failures.service.BCryptPasswordHasher;
import io.github.owasp.lab.common.http.JsonResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Fixed example: Cryptographic Failures.
 * <p>
 * The application stores a BCrypt password hash.
 * BCrypt includes a random salt and cost parameter in the stored hash string.
 */
public final class FixedPasswordRegistrationServlet extends HttpServlet {

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
         * FIX:
         *
         * BCrypt is designed for password storage.
         *
         * The resulting hash string contains:
         *
         * - algorithm/version marker
         * - cost parameter
         * - random salt
         * - password hash
         *
         * The same password will produce different hashes because each hash uses
         * a new random salt.
         */
        var storedPasswordHash = BCryptPasswordHasher.hash(password);

        JsonResponse.ok(response, new PasswordRegistrationResponse(
                username,
                "BCrypt",
                storedPasswordHash,
                "Fixed: BCrypt stores salt and cost inside the hash. Same password produces different hashes."
        ));
    }
}