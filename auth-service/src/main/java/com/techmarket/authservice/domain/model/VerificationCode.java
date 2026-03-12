package com.techmarket.authservice.domain.model;

import java.time.Instant;
import java.util.UUID;

public class VerificationCode {

    private UUID id;
    private String phone;
    private String code;
    private Instant expiresAt;
    private boolean verified;

    public VerificationCode() {
    }

    public VerificationCode(UUID id, String phone, String code,
                            Instant expiresAt, boolean verified) {
        this.id = id;
        this.phone = phone;
        this.code = code;
        this.expiresAt = expiresAt;
        this.verified = verified;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
