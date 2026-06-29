package io.github.owasp.lab.a06_insecure_design.service;

import io.github.owasp.lab.a06_insecure_design.model.OrderStateResponse;
import io.github.owasp.lab.a06_insecure_design.model.OrderStatus;
import io.github.owasp.lab.a06_insecure_design.model.OrderTransitionResponse;

public interface OrderWorkflowService {

    OrderStateResponse getOrder(String orderId);

    OrderTransitionResponse changeStatus(String orderId, OrderStatus requestedStatus);

    OrderStateResponse reset();
}
