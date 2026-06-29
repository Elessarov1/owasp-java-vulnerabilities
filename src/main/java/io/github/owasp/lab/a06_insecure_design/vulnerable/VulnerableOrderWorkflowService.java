package io.github.owasp.lab.a06_insecure_design.vulnerable;

import io.github.owasp.lab.a06_insecure_design.model.OrderStateResponse;
import io.github.owasp.lab.a06_insecure_design.model.OrderStatus;
import io.github.owasp.lab.a06_insecure_design.model.OrderTransitionResponse;
import io.github.owasp.lab.a06_insecure_design.repository.InMemoryOrderRepository;
import io.github.owasp.lab.a06_insecure_design.service.OrderWorkflowService;

import java.util.EnumSet;

public final class VulnerableOrderWorkflowService implements OrderWorkflowService {

    private final InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();

    @Override
    public OrderStateResponse getOrder(String orderId) {
        return state(orderId);
    }

    @Override
    public OrderTransitionResponse changeStatus(String orderId, OrderStatus requestedStatus) {
        var previousStatus = orderRepository.getStatus(orderId);

        /*
         * SECURITY BUG:
         *
         * The service has no model of valid order state transitions. It accepts
         * any target status selected by the client and applies it directly.
         *
         * Input validation cannot fix this design flaw: REFUNDED is a valid enum
         * value, but CREATED -> REFUNDED is not a valid business transition.
         */
        orderRepository.updateStatus(orderId, requestedStatus);

        return new OrderTransitionResponse(
                orderId,
                previousStatus,
                requestedStatus,
                allStatuses(),
                "Vulnerable: client-selected status was applied without workflow validation."
        );
    }

    @Override
    public OrderStateResponse reset() {
        orderRepository.reset();
        return state(InMemoryOrderRepository.DEMO_ORDER_ID);
    }

    private OrderStateResponse state(String orderId) {
        return new OrderStateResponse(orderId, orderRepository.getStatus(orderId), allStatuses());
    }

    private EnumSet<OrderStatus> allStatuses() {
        return EnumSet.allOf(OrderStatus.class);
    }
}
