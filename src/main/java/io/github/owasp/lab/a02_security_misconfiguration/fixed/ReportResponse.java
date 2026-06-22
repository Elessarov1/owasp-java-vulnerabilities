package io.github.owasp.lab.a02_security_misconfiguration.fixed;

import io.github.owasp.lab.a02_security_misconfiguration.shared.DiagnosticReport;

public record ReportResponse(long id, String name, String status, String ownerTeam) {
    public static ReportResponse from(DiagnosticReport report) {
        return new ReportResponse(report.id(), report.name(), report.status(), report.ownerTeam());
    }
}
