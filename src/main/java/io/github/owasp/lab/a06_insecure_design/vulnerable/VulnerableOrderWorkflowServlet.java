package io.github.owasp.lab.a06_insecure_design.vulnerable;

import io.github.owasp.lab.a06_insecure_design.web.OrderWorkflowServlet;

public final class VulnerableOrderWorkflowServlet extends OrderWorkflowServlet {

    public VulnerableOrderWorkflowServlet() {
        super(new VulnerableOrderWorkflowService());
    }
}
