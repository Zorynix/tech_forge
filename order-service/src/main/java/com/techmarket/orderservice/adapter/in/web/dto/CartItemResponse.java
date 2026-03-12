package com.techmarket.orderservice.adapter.in.web.dto;

import com.techmarket.orderservice.domain.model.CartItem;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponse(
        UUID productId,
        String productName,
        BigDecimal price,
        int quantity,
        String imageUrl,
        BigDecimal totalPrice
) {

    public static CartItemResponse from(CartItem item) {
        return new CartItemResponse(
                item.productId(),
                item.productName(),
                item.price(),
                item.quantity(),
                item.imageUrl(),
                item.totalPrice()
        );
    }
}
