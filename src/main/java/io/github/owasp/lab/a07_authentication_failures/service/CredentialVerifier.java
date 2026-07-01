package io.github.owasp.lab.a07_authentication_failures.service;

public interface CredentialVerifier {

    boolean matches(String username, String password);
}
