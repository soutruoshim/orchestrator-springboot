package com.srhdp.orderservice.application.service;

import com.srhdp.orderservice.application.mapper.EntityDtoMapper;
import com.srhdp.orderservice.application.repository.PurchaseOrderRepository;
import com.srhdp.orderservice.common.dto.OrderCreateRequest;
import com.srhdp.orderservice.common.dto.OrderDetails;
import com.srhdp.orderservice.common.dto.PurchaseOrderDto;
import com.srhdp.orderservice.common.service.OrderEventListener;
import com.srhdp.orderservice.common.service.OrderService;
import com.srhdp.orderservice.common.service.WorkflowActionRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final PurchaseOrderRepository repository;
    private final OrderEventListener eventListener;
    private final WorkflowActionRetriever actionRetriever;

    @Override
    public Mono<PurchaseOrderDto> placeOrder(OrderCreateRequest request) {
        var entity = EntityDtoMapper.toPurchaseOrder(request);
        return this.repository.save(entity)
                .map(EntityDtoMapper::toPurchaseOrderDto)
                .doOnNext(eventListener::emitOrderCreated);
    }

    @Override
    public Flux<PurchaseOrderDto> getAllOrders() {
        return this.repository.findAll()
                .map(EntityDtoMapper::toPurchaseOrderDto);
    }

    @Override
    public Mono<OrderDetails> getOrderDetails(UUID orderId) {
        return this.repository.findById(orderId)
                .map(EntityDtoMapper::toPurchaseOrderDto)
                .zipWith(this.actionRetriever.retrieve(orderId).collectList())
                .map(t -> EntityDtoMapper.toOrderDetails(t.getT1(), t.getT2()));
    }

}
