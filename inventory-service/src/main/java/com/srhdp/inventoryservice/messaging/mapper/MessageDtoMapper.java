package com.srhdp.inventoryservice.messaging.mapper;

import com.srhdp.inventoryservice.common.dto.InventoryDeductRequest;
import com.srhdp.inventoryservice.common.dto.OrderInventoryDto;
import com.srhdp.orchestratorcommon.messages.inventory.InventoryRequest;
import com.srhdp.orchestratorcommon.messages.inventory.InventoryResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class MessageDtoMapper {

    public static InventoryDeductRequest toInventoryDeductRequest(InventoryRequest.Deduct request) {
        return InventoryDeductRequest.builder()
                .orderId(request.orderId())
                .productId(request.productId())
                .quantity(request.quantity())
                .build();
    }

    public static InventoryResponse toInventoryDeductedResponse(OrderInventoryDto orderInventoryDto) {
        return InventoryResponse.Deducted.builder()
                .orderId(orderInventoryDto.orderId())
                .inventoryId(orderInventoryDto.inventoryId())
                .productId(orderInventoryDto.productId())
                .quantity(orderInventoryDto.quantity())
                .build();
    }

    public static Function<Throwable, Mono<InventoryResponse>> toInventoryDeclinedResponse(InventoryRequest.Deduct request) {
        return ex -> Mono.fromSupplier(() -> InventoryResponse.Declined.builder()
                .orderId(request.orderId())
                .message(ex.getMessage())
                .build()
        );
    }

}