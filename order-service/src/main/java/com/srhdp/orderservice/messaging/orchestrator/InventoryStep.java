package com.srhdp.orderservice.messaging.orchestrator;

import com.srhdp.orchestratorcommon.messages.Request;
import com.srhdp.orchestratorcommon.messages.inventory.InventoryResponse;
import com.srhdp.orchestratorcommon.orchestrator.WorkflowStep;
import org.reactivestreams.Publisher;

public interface InventoryStep extends WorkflowStep<InventoryResponse> {

    @Override
    default Publisher<Request> process(InventoryResponse response) {
        return switch (response){
            case InventoryResponse.Deducted r -> this.onSuccess(r);
            case InventoryResponse.Declined r -> this.onFailure(r);
        };
    }

    Publisher<Request> onSuccess(InventoryResponse.Deducted response);

    Publisher<Request> onFailure(InventoryResponse.Declined response);

}