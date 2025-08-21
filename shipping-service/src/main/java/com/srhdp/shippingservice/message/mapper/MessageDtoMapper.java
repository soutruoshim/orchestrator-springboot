package com.srhdp.shippingservice.message.mapper;

import com.srhdp.orchestratorcommon.messages.Shipping.ShippingRequest;
import com.srhdp.orchestratorcommon.messages.Shipping.ShippingResponse;
import com.srhdp.shippingservice.common.dto.ScheduleRequest;
import com.srhdp.shippingservice.common.dto.ShipmentDto;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class MessageDtoMapper {

    public static ScheduleRequest toScheduleRequest(ShippingRequest.Schedule request) {
        return ScheduleRequest.builder()
                .customerId(request.customerId())
                .productId(request.productId())
                .quantity(request.quantity())
                .orderId(request.orderId())
                .build();
    }

    public static ShippingResponse toScheduledResponse(ShipmentDto dto) {
        return ShippingResponse.Scheduled.builder()
                .shipmentId(dto.shipmentId())
                .orderId(dto.orderId())
                .deliveryDate(dto.deliveryDate())
                .build();
    }

    public static Function<Throwable, Mono<ShippingResponse>> toShippingDeclinedResponse(ShippingRequest.Schedule request) {
        return ex -> Mono.fromSupplier(() -> ShippingResponse.Declined.builder()
                .orderId(request.orderId())
                .message(ex.getMessage())
                .build()
        );
    }

}
