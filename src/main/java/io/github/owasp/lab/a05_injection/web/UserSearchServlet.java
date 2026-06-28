package io.github.owasp.lab.a05_injection.web;

import io.github.owasp.lab.a05_injection.service.UserSearchService;
import io.github.owasp.lab.common.http.JsonResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public abstract class UserSearchServlet extends HttpServlet {

    private final UserSearchService userSearchService;

    protected UserSearchServlet(UserSearchService userSearchService) {
        this.userSearchService = userSearchService;
    }

    @Override
    protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var username = request.getParameter("username");

        if (username == null || username.isBlank()) {
            JsonResponse.error(response, HttpServletResponse.SC_BAD_REQUEST, "username is required");
            return;
        }

        try {
            JsonResponse.ok(response, userSearchService.findByUsername(username));
        } catch (SQLException exception) {
            JsonResponse.error(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "User search failed");
        }
    }
}
