package com.techmarket.orderservice.domain.event;

import java.time.Instant;
import java.util.UUID;

public record OrderEvent(
        UUID orderId,
        UUID userId,
        String status,
        String phone,
        String message,
        Instant timestamp
) {

    public static OrderEvent of(UUID orderId, UUID userId, String status, String phone, String message) {
        return new OrderEvent(orderId, userId, status, phone, message, Instant.now());
    }
}
