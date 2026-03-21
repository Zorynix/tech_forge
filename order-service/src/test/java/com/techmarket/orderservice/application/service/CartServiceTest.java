package com.techmarket.orderservice.application.service;

import com.techmarket.orderservice.application.port.out.CartStore;
import com.techmarket.orderservice.domain.model.Cart;
import com.techmarket.orderservice.domain.model.CartItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartStore cartStore;

    @InjectMocks
    private CartService cartService;

    @Test
    @DisplayName("getCart should return empty cart when none exists")
    void getCart_noExistingCart_returnsEmpty() {
        UUID userId = UUID.randomUUID();
        when(cartStore.get(userId)).thenReturn(null);

        Cart result = cartService.getCart(userId);

        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("getCart should return existing cart")
    void getCart_existingCart_returnsIt() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(userId, null);
        cart.addItem(new CartItem(UUID.randomUUID(), "iPhone", BigDecimal.valueOf(999), 1, "img.jpg"));
        when(cartStore.get(userId)).thenReturn(cart);

        Cart result = cartService.getCart(userId);

        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    @DisplayName("addToCart should add item and save")
    void addToCart_addsItemAndSaves() {
        UUID userId = UUID.randomUUID();
        when(cartStore.get(userId)).thenReturn(null);
        CartItem item = new CartItem(UUID.randomUUID(), "iPhone", BigDecimal.valueOf(999), 1, "img.jpg");

        Cart result = cartService.addToCart(userId, item);

        assertThat(result.getItems()).hasSize(1);
        verify(cartStore).save(eq(userId), any(Cart.class));
    }

    @Test
    @DisplayName("removeFromCart should remove item and save")
    void removeFromCart_removesItemAndSaves() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Cart cart = new Cart(userId, null);
        cart.addItem(new CartItem(productId, "iPhone", BigDecimal.valueOf(999), 1, "img.jpg"));
        when(cartStore.get(userId)).thenReturn(cart);

        Cart result = cartService.removeFromCart(userId, productId);

        assertThat(result.isEmpty()).isTrue();
        verify(cartStore).save(eq(userId), any(Cart.class));
    }

    @Test
    @DisplayName("clearCart should delete cart")
    void clearCart_deletesFromStore() {
        UUID userId = UUID.randomUUID();

        cartService.clearCart(userId);

        verify(cartStore).delete(userId);
    }
}
