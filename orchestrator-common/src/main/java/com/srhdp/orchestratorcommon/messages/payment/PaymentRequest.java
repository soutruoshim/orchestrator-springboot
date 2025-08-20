package com.srhdp.orchestratorcommon.messages.payment;

import com.srhdp.orchestratorcommon.messages.Request;
import lombok.Builder;

import java.util.UUID;

public sealed interface PaymentRequest extends Request {

    /*
        Intentionally named as Process / Processed as these are inner classes.
        Feel free to change if you do not like it
     */

    @Builder
    record Process(UUID orderId,
                   Integer customerId,
                   Integer amount) implements PaymentRequest {
    }

    @Builder
    record Refund(UUID orderId) implements PaymentRequest {
    }

}
