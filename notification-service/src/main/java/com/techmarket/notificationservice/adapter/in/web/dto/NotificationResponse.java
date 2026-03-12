package com.techmarket.notificationservice.adapter.in.web.dto;

import com.techmarket.notificationservice.domain.model.Notification;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        UUID orderId,
        String channel,
        String message,
        String status,
        Instant sentAt,
        Instant createdAt
) {

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getOrderId(),
                notification.getChannel().name(),
                notification.getMessage(),
                notification.getStatus().name(),
                notification.getSentAt(),
                notification.getCreatedAt()
        );
    }
}
