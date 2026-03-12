package com.techmarket.authservice.domain.model;

import java.time.Instant;
import java.util.UUID;

public class User {

    private UUID id;
    private String phone;
    private String email;
    private String name;
    private Long telegramChatId;
    private Instant createdAt;
    private Instant updatedAt;

    public User() {
    }

    public User(UUID id, String phone, String email, String name, Long telegramChatId,
                Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.telegramChatId = telegramChatId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User createNew(String phone) {
        Instant now = Instant.now();
        return new User(UUID.randomUUID(), phone, null, null, null, now, now);
    }

    public static User createNewWithEmail(String email) {
        Instant now = Instant.now();
        return new User(UUID.randomUUID(), null, email, null, null, now, now);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.updatedAt = Instant.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = Instant.now();
    }

    public Long getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(Long telegramChatId) {
        this.telegramChatId = telegramChatId;
        this.updatedAt = Instant.now();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
