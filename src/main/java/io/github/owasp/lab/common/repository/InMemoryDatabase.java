package io.github.owasp.lab.common.repository;

import java.util.Map;
import java.util.Optional;

public final class InMemoryDatabase {

    private static final Map<Long, User> USERS = Map.of(
            1L, new User(1L, "alice"),
            2L, new User(2L, "bob")
    );

    private static final Map<Long, Document> DOCUMENTS = Map.of(
            100L, new Document(100L, 1L, "Alice private document", "Alice secret content"),
            200L, new Document(200L, 2L, "Bob private document", "Bob secret content")
    );

    private InMemoryDatabase() {
    }

    public static Optional<User> findUserById(long id) {
        return Optional.ofNullable(USERS.get(id));
    }

    public static Optional<Document> findDocumentById(long id) {
        return Optional.ofNullable(DOCUMENTS.get(id));
    }

    public static Optional<Document> findDocumentByIdAndOwnerId(long documentId, long ownerId) {
        return findDocumentById(documentId)
                .filter(document -> document.ownerId() == ownerId);
    }
}
