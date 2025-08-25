package com.srhdp.orderservice.common.service;

import com.srhdp.orderservice.common.dto.OrderWorkflowActionDto;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface WorkflowActionRetriever {

    Flux<OrderWorkflowActionDto> retrieve(UUID orderId);

}
