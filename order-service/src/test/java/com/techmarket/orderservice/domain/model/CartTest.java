package com.techmarket.orderservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartTest {

    private CartItem createItem(String name, BigDecimal price, int qty) {
        return new CartItem(UUID.randomUUID(), name, price, qty, "img.jpg");
    }

    @Test
    @DisplayName("new cart should be empty")
    void newCart_isEmpty() {
        Cart cart = new Cart();
        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.totalPrice()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("addItem should add new item to cart")
    void addItem_newItem() {
        Cart cart = new Cart();
        CartItem item = createItem("iPhone", BigDecimal.valueOf(999), 1);
        cart.addItem(item);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.totalPrice()).isEqualByComparingTo(BigDecimal.valueOf(999));
    }

    @Test
    @DisplayName("addItem should increase quantity for existing product")
    void addItem_existingProduct_increasesQuantity() {
        Cart cart = new Cart();
        UUID productId = UUID.randomUUID();
        CartItem item1 = new CartItem(productId, "iPhone", BigDecimal.valueOf(999), 1, "img.jpg");
        CartItem item2 = new CartItem(productId, "iPhone", BigDecimal.valueOf(999), 2, "img.jpg");

        cart.addItem(item1);
        cart.addItem(item2);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().getFirst().quantity()).isEqualTo(3);
        assertThat(cart.totalPrice()).isEqualByComparingTo(BigDecimal.valueOf(2997));
    }

    @Test
    @DisplayName("removeItem should remove item by productId")
    void removeItem_removesItem() {
        Cart cart = new Cart();
        CartItem item = createItem("iPhone", BigDecimal.valueOf(999), 1);
        cart.addItem(item);
        cart.removeItem(item.productId());

        assertThat(cart.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("clear should remove all items")
    void clear_removesAllItems() {
        Cart cart = new Cart();
        cart.addItem(createItem("iPhone", BigDecimal.valueOf(999), 1));
        cart.addItem(createItem("MacBook", BigDecimal.valueOf(1999), 1));
        cart.clear();

        assertThat(cart.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("CartItem should reject null productId")
    void cartItem_nullProductId_throws() {
        assertThatThrownBy(() -> new CartItem(null, "Test", BigDecimal.TEN, 1, "img.jpg"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("CartItem should reject zero quantity")
    void cartItem_zeroQuantity_throws() {
        assertThatThrownBy(() -> new CartItem(UUID.randomUUID(), "Test", BigDecimal.TEN, 0, "img.jpg"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("CartItem should reject negative price")
    void cartItem_negativePrice_throws() {
        assertThatThrownBy(() -> new CartItem(UUID.randomUUID(), "Test", BigDecimal.valueOf(-1), 1, "img.jpg"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
