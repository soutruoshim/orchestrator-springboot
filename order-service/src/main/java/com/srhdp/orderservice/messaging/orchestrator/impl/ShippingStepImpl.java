package com.srhdp.orderservice.messaging.orchestrator.impl;

import com.srhdp.orchestratorcommon.messages.Request;
import com.srhdp.orchestratorcommon.messages.Shipping.ShippingResponse;
import com.srhdp.orchestratorcommon.orchestrator.RequestCompensator;
import com.srhdp.orchestratorcommon.orchestrator.RequestSender;
import com.srhdp.orderservice.common.enums.WorkflowAction;
import com.srhdp.orderservice.common.service.OrderFulfillmentService;
import com.srhdp.orderservice.common.service.WorkflowActionTracker;
import com.srhdp.orderservice.messaging.mapper.MessageDtoMapper;
import com.srhdp.orderservice.messaging.orchestrator.ShippingStep;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShippingStepImpl implements ShippingStep {

    private final WorkflowActionTracker tracker;
    private final OrderFulfillmentService service;
    private RequestCompensator previousStep;
    private RequestSender nextStep;

    // implement RequestCompensator interface
    @Override
    public Publisher<Request> compensate(UUID orderId) {
        return this.previousStep.compensate(orderId);
    }

    // implement RequestSender interface
    @Override
    public Publisher<Request> send(UUID orderId) {
        return this.tracker.track(orderId, WorkflowAction.SHIPPING_SCHEDULE_INITIATED)
                .then(this.service.get(orderId))
                .map(MessageDtoMapper::toShippingScheduleRequest);
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

    // implement ShippingStep interface
    @Override
    public Publisher<Request> onSuccess(ShippingResponse.Scheduled response) {
        return this.tracker.track(response.orderId(), WorkflowAction.SHIPPING_SCHEDULED)
                .thenReturn(MessageDtoMapper.toShipmentSchedule(response))
                .flatMap(this.service::schedule)
                .thenMany(this.nextStep.send(response.orderId()));
    }

    // implement ShippingStep interface
    @Override
    public Publisher<Request> onFailure(ShippingResponse.Declined response) {
        return this.tracker.track(response.orderId(), WorkflowAction.SHIPPING_DECLINED)
                .thenMany(this.previousStep.compensate(response.orderId()));
    }
}
