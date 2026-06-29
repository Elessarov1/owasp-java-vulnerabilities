package io.github.owasp.lab.a06_insecure_design.fixed;

import io.github.owasp.lab.a06_insecure_design.model.OrderStateResponse;
import io.github.owasp.lab.a06_insecure_design.model.OrderStatus;
import io.github.owasp.lab.a06_insecure_design.model.OrderTransitionResponse;
import io.github.owasp.lab.a06_insecure_design.repository.InMemoryOrderRepository;
import io.github.owasp.lab.a06_insecure_design.service.InvalidOrderTransitionException;
import io.github.owasp.lab.a06_insecure_design.service.OrderWorkflowService;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public final class FixedOrderWorkflowService implements OrderWorkflowService {

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = allowedTransitions();

    private final InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();

    @Override
    public OrderStateResponse getOrder(String orderId) {
        return state(orderId);
    }

    @Override
    public synchronized OrderTransitionResponse changeStatus(String orderId, OrderStatus requestedStatus) {
        var previousStatus = orderRepository.getStatus(orderId);
        var allowedTransitions = transitionsFrom(previousStatus);

        /*
         * FIX:
         *
         * The server owns an explicit state machine and validates every requested
         * transition against business rules before changing persistent state.
         *
         * The check and update are one atomic service operation in this in-memory
         * demo. A production system should enforce the same invariant with a
         * database transaction or optimistic locking.
         */
        if (!allowedTransitions.contains(requestedStatus)) {
            throw new InvalidOrderTransitionException(
                    previousStatus,
                    requestedStatus,
                    allowedTransitions
            );
        }

        orderRepository.updateStatus(orderId, requestedStatus);

        return new OrderTransitionResponse(
                orderId,
                previousStatus,
                requestedStatus,
                transitionsFrom(requestedStatus),
                "Fixed: transition was validated by the server-side state machine."
        );
    }

    @Override
    public OrderStateResponse reset() {
        orderRepository.reset();
        return state(InMemoryOrderRepository.DEMO_ORDER_ID);
    }

    private OrderStateResponse state(String orderId) {
        var status = orderRepository.getStatus(orderId);
        return new OrderStateResponse(orderId, status, transitionsFrom(status));
    }

    private Set<OrderStatus> transitionsFrom(OrderStatus status) {
        return ALLOWED_TRANSITIONS.getOrDefault(status, Set.of());
    }

    private static Map<OrderStatus, Set<OrderStatus>> allowedTransitions() {
        var transitions = new EnumMap<OrderStatus, Set<OrderStatus>>(OrderStatus.class);
        transitions.put(OrderStatus.CREATED, Set.of(OrderStatus.PAID, OrderStatus.CANCELLED));
        transitions.put(OrderStatus.PAID, Set.of(OrderStatus.SHIPPED, OrderStatus.REFUNDED));
        transitions.put(OrderStatus.SHIPPED, Set.of(OrderStatus.DELIVERED));
        return Map.copyOf(transitions);
    }
}
