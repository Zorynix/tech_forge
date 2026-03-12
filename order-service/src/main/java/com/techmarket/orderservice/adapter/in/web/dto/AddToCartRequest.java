package com.techmarket.orderservice.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record AddToCartRequest(
        @NotNull(message = "productId is required")
        UUID productId,

        @NotBlank(message = "productName is required")
        String productName,

        @NotNull(message = "price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "price must be positive")
        BigDecimal price,

        @Min(value = 1, message = "quantity must be at least 1")
        int quantity,

        String imageUrl
) {
}
