package com.srhdp.orderservice.messaging.publisher;

import com.srhdp.orchestratorcommon.publisher.EventPublisher;
import com.srhdp.orderservice.common.dto.PurchaseOrderDto;
import com.srhdp.orderservice.common.service.OrderEventListener;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.UUID;

//@Service
@RequiredArgsConstructor
public class OrderEventListenerImpl implements OrderEventListener, EventPublisher<UUID> {

    private final Sinks.Many<UUID> sink;
    private final Flux<UUID> flux;

    @Override
    public Flux<UUID> publish() {
        return this.flux;
    }

    @Override
    public void emitOrderCreated(PurchaseOrderDto dto) {
        this.sink.emitNext(
                dto.orderId(),
                Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1))
        );
    }

}
