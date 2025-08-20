package com.srhdp.customerpayment.messaging.processor;

import com.srhdp.customerpayment.common.service.PaymentService;
import com.srhdp.customerpayment.messaging.mapper.MessageDtoMapper;
import com.srhdp.orchestratorcommon.exception.EventAlreadyProcessedException;
import com.srhdp.orchestratorcommon.messages.payment.PaymentRequest;
import com.srhdp.orchestratorcommon.messages.payment.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class PaymentRequestProcessorImpl implements PaymentRequestProcessor {

    private final PaymentService service;

    @Override
    public Mono<PaymentResponse> handle(PaymentRequest.Process request) {
        var dto = MessageDtoMapper.toProcessRequest(request);
        return this.service.process(dto)
                .map(MessageDtoMapper::toProcessedResponse)
                .transform(exceptionHandler(request));
    }

    @Override
    public Mono<PaymentResponse> handle(PaymentRequest.Refund request) {
        return this.service.refund(request.orderId())
                .then(Mono.empty());
    }

    private UnaryOperator<Mono<PaymentResponse>> exceptionHandler(PaymentRequest.Process request) {
        return mono -> mono.onErrorResume(EventAlreadyProcessedException.class, ex -> Mono.empty())
                .onErrorResume(MessageDtoMapper.toPaymentDeclinedResponse(request));
    }

}
