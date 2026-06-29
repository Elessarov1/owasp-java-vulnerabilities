package io.github.owasp.lab.a06_insecure_design.web;

import io.github.owasp.lab.a06_insecure_design.model.OrderStatus;
import io.github.owasp.lab.a06_insecure_design.repository.OrderNotFoundException;
import io.github.owasp.lab.a06_insecure_design.service.InvalidOrderTransitionException;
import io.github.owasp.lab.a06_insecure_design.service.OrderWorkflowService;
import io.github.owasp.lab.common.http.JsonResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Locale;

public abstract class OrderWorkflowServlet extends HttpServlet {

    private final OrderWorkflowService orderWorkflowService;

    protected OrderWorkflowServlet(OrderWorkflowService orderWorkflowService) {
        this.orderWorkflowService = orderWorkflowService;
    }

    @Override
    protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var orderId = requiredParameter(request, response, "orderId");

        if (orderId == null) {
            return;
        }

        try {
            JsonResponse.ok(response, orderWorkflowService.getOrder(orderId));
        } catch (OrderNotFoundException exception) {
            JsonResponse.error(response, HttpServletResponse.SC_NOT_FOUND, exception.getMessage());
        }
    }

    @Override
    protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var orderId = requiredParameter(request, response, "orderId");
        var rawStatus = requiredParameter(request, response, "status");

        if (orderId == null || rawStatus == null) {
            return;
        }

        var requestedStatus = parseStatus(rawStatus, response);

        if (requestedStatus == null) {
            return;
        }

        try {
            JsonResponse.ok(response, orderWorkflowService.changeStatus(orderId, requestedStatus));
        } catch (OrderNotFoundException exception) {
            JsonResponse.error(response, HttpServletResponse.SC_NOT_FOUND, exception.getMessage());
        } catch (InvalidOrderTransitionException exception) {
            JsonResponse.error(response, HttpServletResponse.SC_CONFLICT, exception.getMessage());
        }
    }

    @Override
    protected final void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonResponse.ok(response, orderWorkflowService.reset());
    }

    private String requiredParameter(
            HttpServletRequest request,
            HttpServletResponse response,
            String parameterName
    ) throws IOException {
        var value = request.getParameter(parameterName);

        if (value == null || value.isBlank()) {
            JsonResponse.error(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    parameterName + " is required"
            );
            return null;
        }

        return value;
    }

    private OrderStatus parseStatus(String rawStatus, HttpServletResponse response) throws IOException {
        try {
            return OrderStatus.valueOf(rawStatus.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            JsonResponse.error(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Unknown order status: " + rawStatus
            );
            return null;
        }
    }
}
