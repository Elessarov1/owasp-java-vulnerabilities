package io.github.owasp.lab.a07_authentication_failures.vulnerable;

import io.github.owasp.lab.a07_authentication_failures.model.LoginResponse;
import io.github.owasp.lab.a07_authentication_failures.repository.InMemoryCredentialRepository;
import io.github.owasp.lab.a07_authentication_failures.service.AuthenticationService;
import io.github.owasp.lab.a07_authentication_failures.service.CredentialVerifier;
import io.github.owasp.lab.a07_authentication_failures.service.InvalidCredentialsException;

public final class VulnerableAuthenticationService implements AuthenticationService {

    private final CredentialVerifier credentialVerifier;

    public VulnerableAuthenticationService() {
        this(new InMemoryCredentialRepository());
    }

    public VulnerableAuthenticationService(CredentialVerifier credentialVerifier) {
        this.credentialVerifier = credentialVerifier;
    }

    @Override
    public LoginResponse authenticate(String username, String password, String clientId) {
        /*
         * SECURITY BUG:
         *
         * Every request reaches password verification. The service does not
         * count failures, delay repeated attempts, or temporarily block an
         * account or client that is trying many password candidates.
         *
         * An automated attacker can keep guessing until a password succeeds.
         */
        if (!credentialVerifier.matches(username, password)) {
            throw new InvalidCredentialsException();
        }

        return new LoginResponse(true, username, "Authentication successful");
    }

    @Override
    public void reset() {
        // The vulnerable implementation deliberately keeps no attempt state.
    }
}
