package com.techmarket.authservice.adapter.in.web.dto;

import com.techmarket.authservice.domain.model.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String phone,
        String email,
        String name,
        Long telegramChatId,
        Instant createdAt,
        Instant updatedAt
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getPhone(),
                user.getEmail(),
                user.getName(),
                user.getTelegramChatId(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
