package io.github.owasp.lab.a07_authentication_failures.vulnerable;

import io.github.owasp.lab.a07_authentication_failures.web.LoginServlet;

public final class VulnerableLoginServlet extends LoginServlet {

    public VulnerableLoginServlet() {
        super(new VulnerableAuthenticationService());
    }
}
