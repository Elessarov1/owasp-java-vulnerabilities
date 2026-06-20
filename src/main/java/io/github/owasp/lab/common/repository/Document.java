package io.github.owasp.lab.common.repository;

public record Document(long id, long ownerId, String title, String content) {
}
