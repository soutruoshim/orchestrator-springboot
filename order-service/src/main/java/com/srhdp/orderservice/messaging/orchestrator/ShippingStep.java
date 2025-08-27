package com.srhdp.orderservice.messaging.orchestrator;

import com.srhdp.orchestratorcommon.messages.Request;
import com.srhdp.orchestratorcommon.messages.Shipping.ShippingResponse;
import com.srhdp.orchestratorcommon.orchestrator.WorkflowStep;
import org.reactivestreams.Publisher;

public interface ShippingStep extends WorkflowStep<ShippingResponse> {

    @Override
    default Publisher<Request> process(ShippingResponse response) {
        return switch (response){
            case ShippingResponse.Scheduled r -> this.onSuccess(r);
            case ShippingResponse.Declined r -> this.onFailure(r);
        };
    }

    Publisher<Request> onSuccess(ShippingResponse.Scheduled response);

    Publisher<Request> onFailure(ShippingResponse.Declined response);

}
