package io.github.owasp.lab.a01_broken_access_control.fixed;

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
 * Fixed example: object-level authorization.
 * <p>
 * The servlet loads the document by both:
 * - document id
 * - current user id
 * <p>
 * This prevents users from accessing documents that do not belong to them.
 */
public class FixedDocumentServlet extends HttpServlet {

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
         * FIX:
         *
         * The document is loaded with ownership constraint.
         * The current user id comes from trusted authentication context,
         * not from request parameters.
         */
        var document = InMemoryDatabase.findDocumentByIdAndOwnerId(documentId, currentUser.id());

        if (document.isEmpty()) {
            JsonResponse.error(response, HttpServletResponse.SC_NOT_FOUND, "Document not found");
            return;
        }

        JsonResponse.ok(response, document.get());
    }
}