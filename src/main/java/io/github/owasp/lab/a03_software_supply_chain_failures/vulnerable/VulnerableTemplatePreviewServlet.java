package io.github.owasp.lab.a03_software_supply_chain_failures.vulnerable;

import io.github.owasp.lab.a03_software_supply_chain_failures.model.TemplatePreviewResponse;
import io.github.owasp.lab.common.http.JsonResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringSubstitutor;

import java.io.IOException;

/**
 * Vulnerable example: Software Supply Chain Failure.
 * <p>
 * The application uses an outdated third-party dependency and passes
 * user-controlled input directly to a powerful interpolation API.
 */
public final class VulnerableTemplatePreviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var template = request.getParameter("template");

        if (template == null || template.isBlank()) {
            JsonResponse.error(response, HttpServletResponse.SC_BAD_REQUEST, "template is required");
            return;
        }

        /*
         * SECURITY BUG:
         *
         * User-controlled input is evaluated as a template expression.
         *
         * This is dangerous because the application does not treat the input
         * as plain text. Instead, it passes the input to StringSubstitutor
         * created with default interpolators.
         *
         * In vulnerable versions of Apache Commons Text, this kind of usage
         * may expose internal runtime data and, depending on the runtime and
         * available lookups, may lead to more serious impact.
         *
         * The root problem is the combination of:
         *
         * - outdated dependency version
         * - powerful interpolation API
         * - untrusted user-controlled input
         */
        var rendered = StringSubstitutor.createInterpolator().replace(template);

        JsonResponse.ok(response, new TemplatePreviewResponse(template, rendered));
    }
}