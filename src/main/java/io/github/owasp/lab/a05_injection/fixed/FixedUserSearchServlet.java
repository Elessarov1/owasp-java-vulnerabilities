package io.github.owasp.lab.a05_injection.fixed;

import io.github.owasp.lab.a05_injection.web.UserSearchServlet;

/**
 * Fixed example: SQL Injection.
 * <p>
 * The endpoint passes user-controlled input to a service that uses
 * PreparedStatement and binds the value as a SQL parameter.
 */
public final class FixedUserSearchServlet extends UserSearchServlet {

    public FixedUserSearchServlet() {
        super(new FixedUserSearchService());
    }
}
