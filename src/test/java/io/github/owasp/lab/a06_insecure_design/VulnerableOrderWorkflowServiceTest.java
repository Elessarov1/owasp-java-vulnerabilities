package io.github.owasp.lab.a06_insecure_design;

import io.github.owasp.lab.a06_insecure_design.model.OrderStatus;
import io.github.owasp.lab.a06_insecure_design.repository.InMemoryOrderRepository;
import io.github.owasp.lab.a06_insecure_design.vulnerable.VulnerableOrderWorkflowService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VulnerableOrderWorkflowServiceTest {

    @Test
    void acceptsImpossibleTransitionFromCreatedToRefunded() {
        var service = new VulnerableOrderWorkflowService();

        var result = service.changeStatus(
                InMemoryOrderRepository.DEMO_ORDER_ID,
                OrderStatus.REFUNDED
        );

        assertEquals(OrderStatus.CREATED, result.previousStatus());
        assertEquals(OrderStatus.REFUNDED, result.currentStatus());
    }
}
