package io.github.owasp.lab.a03_software_supply_chain_failures.service;

import java.util.Map;
import java.util.Optional;

public final class SafeTemplateCatalog {

    private static final Map<String, String> TEMPLATES = Map.of(
            "welcome", "Hello, ${name}! Welcome to the secure template preview.",
            "reminder", "Hello, ${name}! This is your account reminder.",
            "status", "Hello, ${name}! Your account status is active."
    );

    private SafeTemplateCatalog() {
    }

    public static Optional<String> findByName(String templateName) {
        if (templateName == null || templateName.isBlank()) {
            return Optional.empty();
        }

        return Optional.ofNullable(TEMPLATES.get(templateName));
    }
}
