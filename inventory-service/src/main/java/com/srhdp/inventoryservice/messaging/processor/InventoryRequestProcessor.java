package com.srhdp.inventoryservice.messaging.processor;
import com.srhdp.orchestratorcommon.messages.inventory.InventoryRequest;
import com.srhdp.orchestratorcommon.messages.inventory.InventoryResponse;
import com.srhdp.orchestratorcommon.processor.RequestProcessor;
import reactor.core.publisher.Mono;

public interface InventoryRequestProcessor extends RequestProcessor<InventoryRequest, InventoryResponse> {

    @Override
    default Mono<InventoryResponse> process(InventoryRequest request) {
        return switch (request){
            case InventoryRequest.Deduct r -> this.handle(r);
            case InventoryRequest.Restore r -> this.handle(r);
        };
    }

    Mono<InventoryResponse> handle(InventoryRequest.Deduct request);

    Mono<InventoryResponse> handle(InventoryRequest.Restore request);

}
