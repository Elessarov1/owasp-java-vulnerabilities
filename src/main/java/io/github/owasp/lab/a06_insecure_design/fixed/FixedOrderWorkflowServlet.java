package io.github.owasp.lab.a06_insecure_design.fixed;

import io.github.owasp.lab.a06_insecure_design.web.OrderWorkflowServlet;

public final class FixedOrderWorkflowServlet extends OrderWorkflowServlet {

    public FixedOrderWorkflowServlet() {
        super(new FixedOrderWorkflowService());
    }
}
