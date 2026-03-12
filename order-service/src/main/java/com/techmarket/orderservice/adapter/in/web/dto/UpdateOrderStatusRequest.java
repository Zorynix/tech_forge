package com.techmarket.orderservice.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;

import com.techmarket.orderservice.domain.model.OrderStatus;

public record UpdateOrderStatusRequest(
        @NotNull(message = "status is required")
        OrderStatus status
) {
}
