package com.techmarket.orderservice.adapter.in.web.dto;

import com.techmarket.orderservice.domain.model.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID userId,
        List<OrderItemResponse> items,
        String status,
        BigDecimal totalPrice,
        String phone,
        String email,
        Instant createdAt,
        Instant updatedAt
) {

    public static OrderResponse from(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(OrderItemResponse::from)
                .toList();
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                items,
                order.getStatus().name(),
                order.getTotalPrice(),
                order.getPhone(),
                order.getEmail(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
