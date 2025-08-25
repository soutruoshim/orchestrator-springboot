package com.srhdp.orderservice.application.service;

import com.srhdp.orchestratorcommon.util.DuplicateEventValidator;
import com.srhdp.orderservice.application.mapper.EntityDtoMapper;
import com.srhdp.orderservice.application.repository.OrderWorkflowActionRepository;
import com.srhdp.orderservice.common.dto.OrderWorkflowActionDto;
import com.srhdp.orderservice.common.enums.WorkflowAction;
import com.srhdp.orderservice.common.service.WorkflowActionRetriever;
import com.srhdp.orderservice.common.service.WorkflowActionTracker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkflowActionService implements WorkflowActionTracker, WorkflowActionRetriever {

    private final OrderWorkflowActionRepository repository;

    @Override
    public Flux<OrderWorkflowActionDto> retrieve(UUID orderId) {
        return this.repository.findByOrderIdOrderByCreatedAt(orderId)
                .map(EntityDtoMapper::toOrderWorkflowActionDto);
    }

    @Override
    public Mono<Void> track(UUID orderId, WorkflowAction action) {
        return DuplicateEventValidator.validate(
                this.repository.existsByOrderIdAndAction(orderId, action),
                this.repository.save(EntityDtoMapper.toOrderWorkflowAction(orderId, action)) // defer if required
        ).then();
    }
}