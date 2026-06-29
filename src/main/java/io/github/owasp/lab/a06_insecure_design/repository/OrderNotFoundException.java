package io.github.owasp.lab.a06_insecure_design.repository;

public final class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String orderId) {
        super("Order not found: " + orderId);
    }
}
