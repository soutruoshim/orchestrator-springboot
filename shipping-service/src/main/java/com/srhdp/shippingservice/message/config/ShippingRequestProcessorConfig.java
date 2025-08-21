package com.srhdp.shippingservice.message.config;

import com.srhdp.orchestratorcommon.messages.Shipping.ShippingRequest;
import com.srhdp.orchestratorcommon.messages.Shipping.ShippingResponse;
import com.srhdp.orchestratorcommon.util.MessageConverter;
import com.srhdp.shippingservice.message.processor.ShippingRequestProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class ShippingRequestProcessorConfig {

    private static final Logger log = LoggerFactory.getLogger(ShippingRequestProcessorConfig.class);
    private final ShippingRequestProcessor shippingRequestProcessor;

    @Bean
    public Function<Flux<Message<ShippingRequest>>, Flux<Message<ShippingResponse>>> processor() {
        return flux -> flux.map(MessageConverter::toRecord)
                .doOnNext(r -> log.info("shipping service received {}", r.message()))
                .concatMap(r -> this.shippingRequestProcessor.process(r.message())
                        .doOnSuccess(e -> r.acknowledgement().acknowledge())
                        .doOnError(e -> log.error(e.getMessage()))
                )
                .map(this::toMessage);
    }

    private Message<ShippingResponse> toMessage(ShippingResponse response) {
        log.info("shipping service produced {}", response);
        return MessageBuilder.withPayload(response)
                .setHeader(KafkaHeaders.KEY, response.orderId().toString())
                .build();
    }

}
