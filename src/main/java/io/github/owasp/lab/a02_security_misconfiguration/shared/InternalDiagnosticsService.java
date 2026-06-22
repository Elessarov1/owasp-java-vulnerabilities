package io.github.owasp.lab.a02_security_misconfiguration.shared;

public final class InternalDiagnosticsService {

    public DiagnosticReport getReport(String rawReportId) {
        if (rawReportId == null || rawReportId.isBlank()) {
            throw new IllegalArgumentException(
                    "Missing required parameter 'reportId' for table %s".formatted(InternalDiagnosticsDatabase.tableName())
            );
        }

        final long reportId;
        try {
            reportId = Long.parseLong(rawReportId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid reportId value: '%s'. Expected numeric id for table %s".formatted(rawReportId, InternalDiagnosticsDatabase.tableName()),
                    e
            );
        }

        return InternalDiagnosticsDatabase.findReportById(reportId).orElseThrow(
                () -> new IllegalStateException(
                        "Report %d was not found in %s using database %s and storage path %s".formatted(
                                reportId,
                                InternalDiagnosticsDatabase.tableName(),
                                InternalDiagnosticsDatabase.databaseUrl(),
                                InternalDiagnosticsDatabase.storagePath()
                        )
                ));
    }
}
