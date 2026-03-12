package com.techmarket.orderservice.adapter.out.messaging;

import com.techmarket.orderservice.application.port.out.OrderEventPublisher;
import com.techmarket.orderservice.domain.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderEventPublisher implements OrderEventPublisher {

    private static final String TOPIC = "order-events";

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Override
    public void publish(OrderEvent event) {
        log.info("Publishing order event: orderId={}, status={}", event.orderId(), event.status());
        kafkaTemplate.send(TOPIC, event.orderId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish order event: orderId={}", event.orderId(), ex);
                    } else {
                        log.debug("Order event published successfully: orderId={}, offset={}",
                                event.orderId(), result.getRecordMetadata().offset());
                    }
                });
    }
}
