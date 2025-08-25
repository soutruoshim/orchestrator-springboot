package com.srhdp.orderservice.application.service;

import com.srhdp.orderservice.application.entity.PurchaseOrder;
import com.srhdp.orderservice.application.mapper.EntityDtoMapper;
import com.srhdp.orderservice.application.repository.PurchaseOrderRepository;
import com.srhdp.orderservice.common.dto.OrderShipmentSchedule;
import com.srhdp.orderservice.common.dto.PurchaseOrderDto;
import com.srhdp.orderservice.common.enums.OrderStatus;
import com.srhdp.orderservice.common.service.OrderFulfillmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class OrderFulfillmentServiceImpl implements OrderFulfillmentService {

    private final PurchaseOrderRepository repository;

    @Override
    public Mono<PurchaseOrderDto> get(UUID orderId) {
        return this.repository.findById(orderId)
                .map(EntityDtoMapper::toPurchaseOrderDto);
    }

    @Override
    public Mono<PurchaseOrderDto> schedule(OrderShipmentSchedule shipmentSchedule) {
        return this.update(shipmentSchedule.orderId(), e -> e.setDeliveryDate(shipmentSchedule.deliveryDate()));
    }

    @Override
    public Mono<PurchaseOrderDto> complete(UUID orderId) {
        return this.update(orderId, e -> e.setStatus(OrderStatus.COMPLETED));
    }

    @Override
    public Mono<PurchaseOrderDto> cancel(UUID orderId) {
        return this.update(orderId, e -> e.setStatus(OrderStatus.CANCELLED));
    }

    private Mono<PurchaseOrderDto> update(UUID orderId, Consumer<PurchaseOrder> consumer) {
        return this.repository.findByOrderIdAndStatus(orderId, OrderStatus.PENDING)
                .doOnNext(consumer)
                .flatMap(this.repository::save)
                .map(EntityDtoMapper::toPurchaseOrderDto);
    }

}