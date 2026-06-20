package io.github.owasp.lab.common.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class JsonResponse {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonResponse() {
    }

    public static void ok(HttpServletResponse response, Object body) throws IOException {
        write(response, HttpServletResponse.SC_OK, body);
    }

    public static void error(HttpServletResponse response, int status, String message) throws IOException {
        write(response, status, new ErrorResponse(message));
    }

    public static void write(HttpServletResponse response, int status, Object body) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json");

        OBJECT_MAPPER.writeValue(response.getWriter(), body);
    }

    private record ErrorResponse(String error) {
    }
}