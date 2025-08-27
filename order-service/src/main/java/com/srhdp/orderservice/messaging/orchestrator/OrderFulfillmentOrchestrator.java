package com.srhdp.orderservice.messaging.orchestrator;

import com.srhdp.orchestratorcommon.messages.Request;
import com.srhdp.orchestratorcommon.messages.Response;
import com.srhdp.orchestratorcommon.messages.Shipping.ShippingResponse;
import com.srhdp.orchestratorcommon.messages.inventory.InventoryResponse;
import com.srhdp.orchestratorcommon.messages.payment.PaymentResponse;
import com.srhdp.orchestratorcommon.orchestrator.WorkflowOrchestrator;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public interface OrderFulfillmentOrchestrator extends WorkflowOrchestrator {

    Publisher<Request> orderInitialRequests();

    @Override
    default Publisher<Request> orchestrate(Response response) {
        return switch (response) {
            case PaymentResponse r -> this.handle(r);
            case InventoryResponse r -> this.handle(r);
            case ShippingResponse r -> this.handle(r);
            default -> Mono.empty();
        };
    }

    Publisher<Request> handle(PaymentResponse response);

    Publisher<Request> handle(InventoryResponse response);

    Publisher<Request> handle(ShippingResponse response);

}
