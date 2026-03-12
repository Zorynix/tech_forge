package com.techmarket.notificationservice.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Notification {

    private UUID id;
    private UUID userId;
    private UUID orderId;
    private NotificationChannel channel;
    private String message;
    private NotificationStatus status;
    private Instant sentAt;
    private Instant createdAt;

    public Notification() {
    }

    public Notification(UUID id, UUID userId, UUID orderId, NotificationChannel channel,
                        String message, NotificationStatus status, Instant sentAt, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.orderId = orderId;
        this.channel = channel;
        this.message = message;
        this.status = status;
        this.sentAt = sentAt;
        this.createdAt = createdAt;
    }

    public static Notification createPending(UUID userId, UUID orderId, NotificationChannel channel, String message) {
        return new Notification(
                UUID.randomUUID(),
                userId,
                orderId,
                channel,
                message,
                NotificationStatus.PENDING,
                null,
                Instant.now()
        );
    }

    public void markSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = Instant.now();
    }

    public void markFailed() {
        this.status = NotificationStatus.FAILED;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
