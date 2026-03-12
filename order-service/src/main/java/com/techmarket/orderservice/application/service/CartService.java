package com.techmarket.orderservice.application.service;

import com.techmarket.orderservice.application.port.in.CartUseCase;
import com.techmarket.orderservice.application.port.out.CartStore;
import com.techmarket.orderservice.domain.model.Cart;
import com.techmarket.orderservice.domain.model.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService implements CartUseCase {

    private final CartStore cartStore;

    @Override
    public Cart getCart(UUID userId) {
        Cart cart = cartStore.get(userId);
        if (cart == null) {
            cart = new Cart(userId, null);
        }
        return cart;
    }

    @Override
    public Cart addToCart(UUID userId, CartItem item) {
        Cart cart = getCart(userId);
        cart.setUserId(userId);
        cart.addItem(item);
        cartStore.save(userId, cart);
        return cart;
    }

    @Override
    public Cart removeFromCart(UUID userId, UUID productId) {
        Cart cart = getCart(userId);
        cart.removeItem(productId);
        cartStore.save(userId, cart);
        return cart;
    }

    @Override
    public void clearCart(UUID userId) {
        cartStore.delete(userId);
    }
}
