package com.techmarket.notificationservice.domain.event;

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
}
