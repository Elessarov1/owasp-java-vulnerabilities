package io.github.owasp.lab.a02_security_misconfiguration.fixed;

import io.github.owasp.lab.a02_security_misconfiguration.shared.InternalDiagnosticsService;
import io.github.owasp.lab.common.http.JsonResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class FixedDebugErrorServlet extends HttpServlet {

    private static final System.Logger log = System.getLogger(FixedDebugErrorServlet.class.getName());

    private final InternalDiagnosticsService diagnosticsService = new InternalDiagnosticsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var rawReportId = request.getParameter("reportId");

        try {
            var report = diagnosticsService.getReport(rawReportId);
            JsonResponse.ok(response, ReportResponse.from(report));
        } catch (Exception exception) {
            /*
             * FIX:
             *
             * Detailed error information is written only to server logs.
             *
             * The client receives a generic error message without:
             *
             * - stack trace
             * - internal class names
             * - database URL
             * - table names
             * - file system paths
             * - feature flags
             *
             * Error details are useful for developers,
             * but they must not be exposed through HTTP responses.
             */
            log.log(System.Logger.Level.ERROR, "Failed to load diagnostic report", exception);

            JsonResponse.error(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal server error"
            );
        }
    }
}
