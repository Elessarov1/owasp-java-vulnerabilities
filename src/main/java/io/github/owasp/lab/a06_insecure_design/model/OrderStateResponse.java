package io.github.owasp.lab.a06_insecure_design.model;

import java.util.Set;

public record OrderStateResponse(
        String orderId,
        OrderStatus status,
        Set<OrderStatus> allowedTransitions
) {
}
