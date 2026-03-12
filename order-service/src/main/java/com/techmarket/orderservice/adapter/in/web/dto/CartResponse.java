package com.techmarket.orderservice.adapter.in.web.dto;

import com.techmarket.orderservice.domain.model.Cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponse(
        UUID userId,
        List<CartItemResponse> items,
        BigDecimal totalPrice
) {

    public static CartResponse from(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(CartItemResponse::from)
                .toList();
        return new CartResponse(cart.getUserId(), items, cart.totalPrice());
    }
}
