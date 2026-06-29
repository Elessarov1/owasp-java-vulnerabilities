package io.github.owasp.lab.a06_insecure_design.repository;

import io.github.owasp.lab.a06_insecure_design.model.OrderStatus;

import java.util.HashMap;
import java.util.Map;

public final class InMemoryOrderRepository {

    public static final String DEMO_ORDER_ID = "ORDER-1001";

    private final Map<String, OrderStatus> orders = new HashMap<>();

    public InMemoryOrderRepository() {
        reset();
    }

    public synchronized OrderStatus getStatus(String orderId) {
        var status = orders.get(orderId);

        if (status == null) {
            throw new OrderNotFoundException(orderId);
        }

        return status;
    }

    public synchronized void updateStatus(String orderId, OrderStatus status) {
        if (!orders.containsKey(orderId)) {
            throw new OrderNotFoundException(orderId);
        }

        orders.put(orderId, status);
    }

    public synchronized void reset() {
        orders.clear();
        orders.put(DEMO_ORDER_ID, OrderStatus.CREATED);
    }
}
