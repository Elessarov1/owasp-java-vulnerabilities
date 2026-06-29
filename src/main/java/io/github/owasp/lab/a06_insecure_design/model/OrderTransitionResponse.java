package io.github.owasp.lab.a06_insecure_design.model;

import java.util.Set;

public record OrderTransitionResponse(
        String orderId,
        OrderStatus previousStatus,
        OrderStatus currentStatus,
        Set<OrderStatus> allowedTransitions,
        String message
) {
}
