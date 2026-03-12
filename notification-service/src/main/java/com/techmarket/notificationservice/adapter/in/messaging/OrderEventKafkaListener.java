package com.techmarket.notificationservice.adapter.in.messaging;

import com.techmarket.notificationservice.application.port.in.ProcessOrderEventUseCase;
import com.techmarket.notificationservice.domain.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventKafkaListener {

    private final ProcessOrderEventUseCase processOrderEventUseCase;

    @KafkaListener(
            topics = "order-events",
            groupId = "notification-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onOrderEvent(OrderEvent event) {
        log.info("Received order event: orderId={}, status={}", event.orderId(), event.status());
        try {
            processOrderEventUseCase.process(event);
        } catch (Exception e) {
            log.error("Error processing order event: orderId={}", event.orderId(), e);
        }
    }
}
