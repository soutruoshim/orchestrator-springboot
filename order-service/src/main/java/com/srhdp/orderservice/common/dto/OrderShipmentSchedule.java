package com.srhdp.orderservice.common.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record OrderShipmentSchedule(UUID orderId,
                                    Instant deliveryDate) {
}
