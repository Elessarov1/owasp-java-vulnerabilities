package io.github.owasp.lab.a05_injection.vulnerable;

import io.github.owasp.lab.a05_injection.model.UserSearchResponse;
import io.github.owasp.lab.a05_injection.repository.VulnerableUserRepository;
import io.github.owasp.lab.a05_injection.service.UserSearchService;

import java.sql.SQLException;

public final class VulnerableUserSearchService implements UserSearchService {

    private final VulnerableUserRepository userRepository = new VulnerableUserRepository();

    @Override
    public UserSearchResponse findByUsername(String username) throws SQLException {
        return userRepository.findByUsername(username);
    }
}
