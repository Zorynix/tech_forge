package com.techmarket.orderservice.application.port.in;

import com.techmarket.orderservice.domain.model.Cart;
import com.techmarket.orderservice.domain.model.CartItem;

import java.util.UUID;

public interface CartUseCase {

    Cart getCart(UUID userId);

    Cart addToCart(UUID userId, CartItem item);

    Cart removeFromCart(UUID userId, UUID productId);

    void clearCart(UUID userId);
}
