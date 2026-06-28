package io.github.owasp.lab.a05_injection.service;

import io.github.owasp.lab.a05_injection.model.UserSearchResponse;

import java.sql.SQLException;

public interface UserSearchService {

    UserSearchResponse findByUsername(String username) throws SQLException;
}
