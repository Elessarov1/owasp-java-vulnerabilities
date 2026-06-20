package io.github.owasp.lab.a01_broken_access_control.vulnerable;

import io.github.owasp.lab.common.auth.CurrentUser;
import io.github.owasp.lab.common.auth.UserContext;
import io.github.owasp.lab.common.http.JsonResponse;
import io.github.owasp.lab.common.http.RequestParameters;
import io.github.owasp.lab.common.repository.InMemoryDatabase;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Vulnerable example: IDOR / Broken Access Control.
 * <p>
 * The user is authenticated, but the servlet does not check
 * that the requested document belongs to the current user.
 */
public class VulnerableDocumentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var currentUser = (CurrentUser) request.getAttribute(UserContext.CURRENT_USER_ATTRIBUTE);

        if (currentUser == null) {
            JsonResponse.error(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        var documentId = RequestParameters.getRequiredLong(request, response, "id");
        if (documentId == null) {
            return;
        }

        /*
         * SECURITY BUG:
         *
         * The document is loaded only by document id.
         * There is no ownership check:
         *
         * document.ownerId() == currentUser.id()
         *
         * As a result, Alice can request Bob's document
         * if she knows or guesses its id.
         */
        var document = InMemoryDatabase.findDocumentById(documentId);

        if (document.isEmpty()) {
            JsonResponse.error(response, HttpServletResponse.SC_NOT_FOUND, "Document not found");
            return;
        }

        JsonResponse.ok(response, document.get());
    }
}