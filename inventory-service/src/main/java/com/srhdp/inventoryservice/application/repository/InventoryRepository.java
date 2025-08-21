package com.srhdp.inventoryservice.application.repository;

import com.srhdp.inventoryservice.application.entity.OrderInventory;
import com.srhdp.orchestratorcommon.messages.inventory.InventoryStatus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface InventoryRepository extends ReactiveCrudRepository<OrderInventory, UUID> {

    Mono<Boolean> existsByOrderId(UUID orderId);

    Mono<OrderInventory> findByOrderIdAndStatus(UUID orderId, InventoryStatus status);

}
