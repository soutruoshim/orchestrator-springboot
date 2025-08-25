package com.srhdp.orderservice.common.service;
import com.srhdp.orderservice.common.enums.WorkflowAction;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface WorkflowActionTracker {
    Mono<Void> track(UUID orderId, WorkflowAction action);
}
