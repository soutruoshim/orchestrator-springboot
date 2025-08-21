package com.srhdp.shippingservice.application.service;

import com.srhdp.orchestratorcommon.messages.Shipping.ShippingStatus;
import com.srhdp.orchestratorcommon.util.DuplicateEventValidator;
import com.srhdp.shippingservice.application.entity.Shipment;
import com.srhdp.shippingservice.application.mapper.EntityDtoMapper;
import com.srhdp.shippingservice.application.repository.ShipmentRepository;
import com.srhdp.shippingservice.common.dto.ScheduleRequest;
import com.srhdp.shippingservice.common.dto.ShipmentDto;
import com.srhdp.shippingservice.common.exception.ShipmentQuantityLimitExceededException;
import com.srhdp.shippingservice.common.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

    private static final Mono<Shipment> LIMIT_EXCEEDED = Mono.error(new ShipmentQuantityLimitExceededException());
    private final ShipmentRepository repository;

    @Override
    public Mono<ShipmentDto> schedule(ScheduleRequest request) {
        return DuplicateEventValidator.validate(
                        this.repository.existsByOrderId(request.orderId()),
                        Mono.just(request)
                )
                .filter(r -> r.quantity() < 10)
                .map(EntityDtoMapper::toShipment)
                .switchIfEmpty(LIMIT_EXCEEDED)
                .flatMap(this::schedule);
    }

    private Mono<ShipmentDto> schedule(Shipment shipment) {
        shipment.setDeliveryDate(Instant.now().plus(Duration.ofDays(3)));
        shipment.setStatus(ShippingStatus.SCHEDULED);
        return this.repository.save(shipment)
                .map(EntityDtoMapper::toDto);
    }

}