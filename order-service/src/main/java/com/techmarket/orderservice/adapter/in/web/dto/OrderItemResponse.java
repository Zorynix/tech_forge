package com.techmarket.orderservice.adapter.in.web.dto;

import com.techmarket.orderservice.domain.model.OrderItem;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID productId,
        String productName,
        BigDecimal price,
        int quantity,
        String imageUrl,
        BigDecimal totalPrice
) {

    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.productId(),
                item.productName(),
                item.price(),
                item.quantity(),
                item.imageUrl(),
                item.totalPrice()
        );
    }
}
