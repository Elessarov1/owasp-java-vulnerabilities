package io.github.owasp.lab.common.http;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class RequestParameters {

    private RequestParameters() {
    }

    public static Long getRequiredLong(HttpServletRequest request, HttpServletResponse response, String parameterName) throws IOException {
        var rawValue = request.getParameter(parameterName);

        if (rawValue == null || rawValue.isBlank()) {
            JsonResponse.error(response, HttpServletResponse.SC_BAD_REQUEST, "Missing parameter: " + parameterName);
            return null;
        }

        try {
            return Long.parseLong(rawValue);
        } catch (NumberFormatException e) {
            JsonResponse.error(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter: " + parameterName);
            return null;
        }
    }
}