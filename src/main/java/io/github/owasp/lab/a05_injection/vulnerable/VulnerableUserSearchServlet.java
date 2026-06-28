package io.github.owasp.lab.a05_injection.vulnerable;

import io.github.owasp.lab.a05_injection.web.UserSearchServlet;

/**
 * Vulnerable example: SQL Injection.
 * <p>
 * The endpoint passes user-controlled input to a service that builds SQL
 * through string concatenation.
 */
public final class VulnerableUserSearchServlet extends UserSearchServlet {

    public VulnerableUserSearchServlet() {
        super(new VulnerableUserSearchService());
    }
}
