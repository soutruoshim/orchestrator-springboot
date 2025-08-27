package com.srhdp.orderservice.messaging.orchestrator.impl;

import com.srhdp.orchestratorcommon.messages.Request;
import com.srhdp.orchestratorcommon.messages.inventory.InventoryResponse;
import com.srhdp.orchestratorcommon.orchestrator.RequestCompensator;
import com.srhdp.orchestratorcommon.orchestrator.RequestSender;
import com.srhdp.orderservice.common.enums.WorkflowAction;
import com.srhdp.orderservice.common.service.OrderFulfillmentService;
import com.srhdp.orderservice.common.service.WorkflowActionTracker;
import com.srhdp.orderservice.messaging.mapper.MessageDtoMapper;
import com.srhdp.orderservice.messaging.orchestrator.InventoryStep;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryStepImpl implements InventoryStep {

    private final WorkflowActionTracker tracker;
    private final OrderFulfillmentService service;
    private RequestCompensator previousStep;
    private RequestSender nextStep;

    // implement RequestCompensator interface
    @Override
    public Publisher<Request> compensate(UUID orderId) {
        return this.tracker.track(orderId, WorkflowAction.INVENTORY_RESTORE_INITIATED)
                .<Request>thenReturn(MessageDtoMapper.toInventoryRestoreRequest(orderId))
                .concatWith(this.previousStep.compensate(orderId));
    }

    // implement RequestSender interface
    @Override
    public Publisher<Request> send(UUID orderId) {
        return this.tracker.track(orderId, WorkflowAction.INVENTORY_REQUEST_INITIATED)
                .then(this.service.get(orderId))
                .map(MessageDtoMapper::toInventoryDeductRequest);
    }

    // implement WorkFlowChain interface
    @Override
    public void setPreviousStep(RequestCompensator previousStep) {
        this.previousStep = previousStep;
    }


    // implement WorkFlowChain interface
    @Override
    public void setNextStep(RequestSender nextStep) {
        this.nextStep = nextStep;
    }

    // implement InventoryStep interface
    @Override
    public Publisher<Request> onSuccess(InventoryResponse.Deducted response) {
        return this.tracker.track(response.orderId(), WorkflowAction.INVENTORY_DEDUCTED)
                .thenMany(this.nextStep.send(response.orderId()));
        // also Mono.from(...) can be used if we know for sure it is going to be only one request
    }

    // implement InventoryStep interface
    @Override
    public Publisher<Request> onFailure(InventoryResponse.Declined response) {
        return this.tracker.track(response.orderId(), WorkflowAction.INVENTORY_DECLINED)
                .thenMany(this.previousStep.compensate(response.orderId()));
    }
}
