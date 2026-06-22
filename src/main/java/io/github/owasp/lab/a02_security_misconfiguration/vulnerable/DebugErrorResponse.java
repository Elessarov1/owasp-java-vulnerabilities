package io.github.owasp.lab.a02_security_misconfiguration.vulnerable;

public record DebugErrorResponse(
        String error,
        String exceptionType,
        String exceptionMessage,
        String databaseUrl,
        String tableName,
        String storagePath,
        String featureFlag,
        String stackTrace
) {
}
