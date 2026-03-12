package com.techmarket.orderservice.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItem(
        UUID productId,
        String productName,
        BigDecimal price,
        int quantity,
        String imageUrl
) {

    public BigDecimal totalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public static OrderItem fromCartItem(CartItem cartItem) {
        return new OrderItem(
                cartItem.productId(),
                cartItem.productName(),
                cartItem.price(),
                cartItem.quantity(),
                cartItem.imageUrl()
        );
    }
}
