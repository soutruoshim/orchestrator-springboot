package com.srhdp.shippingservice.common.dto;

import com.srhdp.orchestratorcommon.messages.Shipping.ShippingStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ShipmentDto(UUID shipmentId,
                          UUID orderId,
                          Integer productId,
                          Integer customerId,
                          Integer quantity,
                          Instant deliveryDate,
                          ShippingStatus status) {
}
