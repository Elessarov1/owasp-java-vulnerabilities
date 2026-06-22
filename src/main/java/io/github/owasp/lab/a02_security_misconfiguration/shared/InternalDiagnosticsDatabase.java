package io.github.owasp.lab.a02_security_misconfiguration.shared;

import java.util.Map;
import java.util.Optional;

public final class InternalDiagnosticsDatabase {

    private static final String DATABASE_URL = "jdbc:postgresql://internal-postgres:5432/owasp_lab";
    private static final String TABLE_NAME = "admin.internal_diagnostic_reports";
    private static final String STORAGE_PATH = "/opt/owasp-lab/private/diagnostics";
    private static final String FEATURE_FLAG = "debug.diagnostics.enabled=true";

    private static final Map<Long, DiagnosticReport> REPORTS = Map.of(
            1L, new DiagnosticReport(1L, "Daily security scan", "FAILED", "appsec"),
            2L, new DiagnosticReport(2L, "Dependency audit", "COMPLETED", "platform")
    );

    private InternalDiagnosticsDatabase() {
    }

    public static Optional<DiagnosticReport> findReportById(long id) {
        return Optional.ofNullable(REPORTS.get(id));
    }

    public static String databaseUrl() {
        return DATABASE_URL;
    }

    public static String tableName() {
        return TABLE_NAME;
    }

    public static String storagePath() {
        return STORAGE_PATH;
    }

    public static String featureFlag() {
        return FEATURE_FLAG;
    }
}
