package io.github.owasp.lab.a07_authentication_failures.service;

import io.github.owasp.lab.a07_authentication_failures.model.LoginResponse;

public interface AuthenticationService {

    LoginResponse authenticate(String username, String password, String clientId);

    void reset();
}
