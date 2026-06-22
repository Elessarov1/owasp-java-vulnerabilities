package io.github.owasp.lab.a02_security_misconfiguration.vulnerable;

import io.github.owasp.lab.a02_security_misconfiguration.shared.InternalDiagnosticsDatabase;
import io.github.owasp.lab.a02_security_misconfiguration.shared.InternalDiagnosticsService;
import io.github.owasp.lab.common.http.JsonResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class VulnerableDebugErrorServlet extends HttpServlet {

    private final InternalDiagnosticsService diagnosticsService = new InternalDiagnosticsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var rawReportId = request.getParameter("reportId");

        try {
            var report = diagnosticsService.getReport(rawReportId);

            JsonResponse.ok(response, ReportResponse.from(report));
        } catch (Exception exception) {
            /*
             * SECURITY BUG:
             *
             * The application returns internal diagnostic details
             * directly to the client.
             *
             * The response exposes:
             *
             * - exception type
             * - exception message
             * - database URL
             * - internal table name
             * - file system path
             * - feature flag
             * - full stack trace
             *
             * This information helps attackers understand the internal
             * application structure and can make further attacks easier.
             */
            var body = new DebugErrorResponse(
                    "Debug endpoint failed",
                    exception.getClass().getName(),
                    exception.getMessage(),
                    InternalDiagnosticsDatabase.databaseUrl(),
                    InternalDiagnosticsDatabase.tableName(),
                    InternalDiagnosticsDatabase.storagePath(),
                    InternalDiagnosticsDatabase.featureFlag(),
                    stackTraceToString(exception)
            );

            JsonResponse.write(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, body);
        }
    }

    private static String stackTraceToString(Exception exception) {
        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter);

        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
