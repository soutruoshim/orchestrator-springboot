package com.srhdp.inventoryservice.common.dto;

import com.srhdp.orchestratorcommon.messages.inventory.InventoryStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderInventoryDto(UUID inventoryId,
                                UUID orderId,
                                Integer productId,
                                Integer quantity,
                                InventoryStatus status) {
}
