package io.github.owasp.lab.a03_software_supply_chain_failures.fixed;

import io.github.owasp.lab.a03_software_supply_chain_failures.model.TemplatePreviewResponse;
import io.github.owasp.lab.a03_software_supply_chain_failures.service.SafeTemplateCatalog;
import io.github.owasp.lab.common.http.JsonResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringSubstitutor;

import java.io.IOException;
import java.util.Map;

/**
 * Fixed example: Software Supply Chain Failure.
 * <p>
 * The fixed version does not evaluate arbitrary user-controlled templates.
 * The user selects a predefined server-side template and provides only simple
 * values for known placeholders.
 */
public final class FixedTemplatePreviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var templateName = request.getParameter("templateName");
        var name = request.getParameter("name");

        if (templateName == null || templateName.isBlank()) {
            JsonResponse.error(response, HttpServletResponse.SC_BAD_REQUEST, "templateName is required");
            return;
        }

        if (name == null || name.isBlank()) {
            JsonResponse.error(response, HttpServletResponse.SC_BAD_REQUEST, "name is required");
            return;
        }

        var template = SafeTemplateCatalog.findByName(templateName);

        if (template.isEmpty()) {
            JsonResponse.error(response, HttpServletResponse.SC_NOT_FOUND, "Template not found");
            return;
        }

        /*
         * FIX:
         *
         * The user does not control the template expression.
         *
         * The template is selected from a predefined server-side catalog.
         * The substitution engine receives only a restricted map of allowed
         * placeholder values.
         *
         * This code does not use createInterpolator() and therefore does not
         * enable broad default lookup mechanisms.
         */
        var values = Map.of(
                "name", name
        );

        var substitutor = new StringSubstitutor(values);
        var rendered = substitutor.replace(template.get());

        JsonResponse.ok(response, new TemplatePreviewResponse(template.get(), rendered));
    }
}