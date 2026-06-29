package io.github.owasp.lab.a06_insecure_design.service;

import io.github.owasp.lab.a06_insecure_design.model.OrderStatus;

import java.util.Set;

public final class InvalidOrderTransitionException extends RuntimeException {

    public InvalidOrderTransitionException(
            OrderStatus currentStatus,
            OrderStatus requestedStatus,
            Set<OrderStatus> allowedTransitions
    ) {
        super("Transition from %s to %s is not allowed. Allowed transitions: %s"
                .formatted(currentStatus, requestedStatus, allowedTransitions));
    }
}
