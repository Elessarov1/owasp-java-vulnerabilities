package io.github.owasp.lab.a07_authentication_failures.web;

import io.github.owasp.lab.a07_authentication_failures.model.ResetResponse;
import io.github.owasp.lab.a07_authentication_failures.service.AuthenticationService;
import io.github.owasp.lab.a07_authentication_failures.service.InvalidCredentialsException;
import io.github.owasp.lab.a07_authentication_failures.service.TooManyAttemptsException;
import io.github.owasp.lab.common.http.JsonResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public abstract class LoginServlet extends HttpServlet {

    private static final int SC_TOO_MANY_REQUESTS = 429;

    private final AuthenticationService authenticationService;

    protected LoginServlet(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
         * In this local lab, getRemoteAddr() identifies the request source.
         * Production systems behind a proxy should use only forwarding headers
         * set and sanitized by a trusted proxy.
         */
        var clientId = request.getRemoteAddr();

        try {
            JsonResponse.ok(
                    response,
                    authenticationService.authenticate(username, password, clientId)
            );
        } catch (InvalidCredentialsException exception) {
            JsonResponse.error(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    exception.getMessage()
            );
        } catch (TooManyAttemptsException exception) {
            response.setHeader("Retry-After", Long.toString(exception.retryAfterSeconds()));
            JsonResponse.error(
                    response,
                    SC_TOO_MANY_REQUESTS,
                    exception.getMessage()
            );
        }
    }

    @Override
    protected final void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.reset();
        JsonResponse.ok(response, new ResetResponse("Login attempt state was reset."));
    }
}
