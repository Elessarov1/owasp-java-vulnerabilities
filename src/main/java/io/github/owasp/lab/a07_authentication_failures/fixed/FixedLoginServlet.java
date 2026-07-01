package io.github.owasp.lab.a07_authentication_failures.fixed;

import io.github.owasp.lab.a07_authentication_failures.web.LoginServlet;

public final class FixedLoginServlet extends LoginServlet {

    public FixedLoginServlet() {
        super(new FixedAuthenticationService());
    }
}
