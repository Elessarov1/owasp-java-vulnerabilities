package io.github.owasp.lab.a06_insecure_design;

import io.github.owasp.lab.a06_insecure_design.fixed.FixedOrderWorkflowService;
import io.github.owasp.lab.a06_insecure_design.model.OrderStatus;
import io.github.owasp.lab.a06_insecure_design.repository.InMemoryOrderRepository;
import io.github.owasp.lab.a06_insecure_design.service.InvalidOrderTransitionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FixedOrderWorkflowServiceTest {

    @Test
    void rejectsTransitionFromCreatedToRefunded() {
        var service = new FixedOrderWorkflowService();

        assertThrows(
                InvalidOrderTransitionException.class,
                () -> service.changeStatus(
                        InMemoryOrderRepository.DEMO_ORDER_ID,
                        OrderStatus.REFUNDED
                )
        );

        assertEquals(
                OrderStatus.CREATED,
                service.getOrder(InMemoryOrderRepository.DEMO_ORDER_ID).status()
        );
    }

    @Test
    void acceptsTransitionsDefinedByStateMachine() {
        var service = new FixedOrderWorkflowService();

        service.changeStatus(InMemoryOrderRepository.DEMO_ORDER_ID, OrderStatus.PAID);
        service.changeStatus(InMemoryOrderRepository.DEMO_ORDER_ID, OrderStatus.SHIPPED);
        var result = service.changeStatus(
                InMemoryOrderRepository.DEMO_ORDER_ID,
                OrderStatus.DELIVERED
        );

        assertEquals(OrderStatus.DELIVERED, result.currentStatus());
    }
}
