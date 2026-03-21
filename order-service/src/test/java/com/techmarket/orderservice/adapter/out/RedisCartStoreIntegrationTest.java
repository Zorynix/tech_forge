package com.techmarket.orderservice.adapter.out;

import com.techmarket.orderservice.domain.model.Cart;
import com.techmarket.orderservice.domain.model.CartItem;
import com.techmarket.orderservice.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RedisCartStoreIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RedisCartStore cartStore;

    @Test
    @DisplayName("should save and retrieve cart")
    void saveAndGet() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(userId, null);
        cart.addItem(new CartItem(UUID.randomUUID(), "iPhone", BigDecimal.valueOf(999), 1, "img.jpg"));

        cartStore.save(userId, cart);
        Cart result = cartStore.get(userId);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().getFirst().productName()).isEqualTo("iPhone");
    }

    @Test
    @DisplayName("should delete cart")
    void delete() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(userId, null);
        cart.addItem(new CartItem(UUID.randomUUID(), "MacBook", BigDecimal.valueOf(1999), 1, "img.jpg"));

        cartStore.save(userId, cart);
        cartStore.delete(userId);

        Cart result = cartStore.get(userId);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("should return null for non-existent cart")
    void get_nonExistent_returnsNull() {
        Cart result = cartStore.get(UUID.randomUUID());
        assertThat(result).isNull();
    }
}
