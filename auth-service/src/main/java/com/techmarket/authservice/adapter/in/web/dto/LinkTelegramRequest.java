package com.techmarket.authservice.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;

public record LinkTelegramRequest(
        @NotNull(message = "Chat ID is required")
        Long chatId
) {
}
