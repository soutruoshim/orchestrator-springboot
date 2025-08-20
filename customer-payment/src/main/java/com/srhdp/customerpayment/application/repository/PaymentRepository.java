package com.srhdp.customerpayment.application.repository;

import com.srhdp.customerpayment.application.entity.CustomerPayment;
import com.srhdp.orchestratorcommon.messages.payment.PaymentStatus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface PaymentRepository extends ReactiveCrudRepository<CustomerPayment, UUID> {

    Mono<Boolean> existsByOrderId(UUID orderId);

    Mono<CustomerPayment> findByOrderIdAndStatus(UUID orderId, PaymentStatus status);

}
