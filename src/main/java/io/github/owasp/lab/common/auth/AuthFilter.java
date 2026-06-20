package io.github.owasp.lab.common.auth;

import io.github.owasp.lab.common.http.JsonResponse;
import io.github.owasp.lab.common.repository.InMemoryDatabase;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthFilter implements Filter {

    private static final String USER_ID_HEADER = "X-User-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;
        var rawUserId = httpRequest.getHeader(USER_ID_HEADER);

        if (rawUserId == null || rawUserId.isBlank()) {
            JsonResponse.error(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Missing X-User-Id header");
            return;
        }

        final long userId;
        try {
            userId = Long.parseLong(rawUserId);
        } catch (NumberFormatException e) {
            JsonResponse.error(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Invalid X-User-Id header");
            return;
        }

        var user = InMemoryDatabase.findUserById(userId);
        if (user.isEmpty()) {
            JsonResponse.error(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Unknown user");
            return;
        }

        var currentUser = new CurrentUser(user.get().id(), user.get().username());
        httpRequest.setAttribute(UserContext.CURRENT_USER_ATTRIBUTE, currentUser);

        chain.doFilter(request, response);
    }
}