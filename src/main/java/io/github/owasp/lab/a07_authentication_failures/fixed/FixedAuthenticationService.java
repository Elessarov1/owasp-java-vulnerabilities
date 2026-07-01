package io.github.owasp.lab.a07_authentication_failures.fixed;

import io.github.owasp.lab.a07_authentication_failures.model.LoginResponse;
import io.github.owasp.lab.a07_authentication_failures.repository.InMemoryCredentialRepository;
import io.github.owasp.lab.a07_authentication_failures.service.AuthenticationService;
import io.github.owasp.lab.a07_authentication_failures.service.CredentialVerifier;
import io.github.owasp.lab.a07_authentication_failures.service.InvalidCredentialsException;

public final class FixedAuthenticationService implements AuthenticationService {

    private final CredentialVerifier credentialVerifier;
    private final LoginAttemptLimiter attemptLimiter;

    public FixedAuthenticationService() {
        this(new InMemoryCredentialRepository(), new LoginAttemptLimiter());
    }

    public FixedAuthenticationService(
            CredentialVerifier credentialVerifier,
            LoginAttemptLimiter attemptLimiter
    ) {
        this.credentialVerifier = credentialVerifier;
        this.attemptLimiter = attemptLimiter;
    }

    @Override
    public LoginResponse authenticate(String username, String password, String clientId) {
        /*
         * FIX:
         *
         * Limits are checked before the expensive credential verification.
         * Failures are tracked for both the account identifier and the request
         * source. The block is temporary to reduce account lockout abuse.
         */
        attemptLimiter.checkAllowed(username, clientId);

        if (!credentialVerifier.matches(username, password)) {
            attemptLimiter.recordFailure(username, clientId);
            throw new InvalidCredentialsException();
        }

        attemptLimiter.recordSuccess(username, clientId);
        return new LoginResponse(true, username, "Authentication successful");
    }

    @Override
    public void reset() {
        attemptLimiter.reset();
    }
}
