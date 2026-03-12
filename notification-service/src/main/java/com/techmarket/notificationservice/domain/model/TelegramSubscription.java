package com.techmarket.notificationservice.domain.model;

import java.time.Instant;
import java.util.UUID;

public class TelegramSubscription {

    private UUID id;
    private UUID userId;
    private Long chatId;
    private String phone;
    private boolean active;
    private Instant createdAt;

    public TelegramSubscription() {
    }

    public TelegramSubscription(UUID id, UUID userId, Long chatId, String phone,
                                boolean active, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.chatId = chatId;
        this.phone = phone;
        this.active = active;
        this.createdAt = createdAt;
    }

    public static TelegramSubscription createNew(String phone, Long chatId) {
        return new TelegramSubscription(
                UUID.randomUUID(),
                null,
                chatId,
                phone,
                true,
                Instant.now()
        );
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

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
