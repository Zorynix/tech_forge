package com.techmarket.orderservice.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItem(
        UUID productId,
        String productName,
        BigDecimal price,
        int quantity,
        String imageUrl
) {

    public CartItem {
        if (productId == null) throw new IllegalArgumentException("productId must not be null");
        if (productName == null || productName.isBlank()) throw new IllegalArgumentException("productName must not be blank");
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("price must not be negative");
        if (quantity < 1) throw new IllegalArgumentException("quantity must be at least 1");
    }

    public BigDecimal totalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
