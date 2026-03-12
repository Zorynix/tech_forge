package com.techmarket.orderservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cart {

    private UUID userId;
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public Cart(UUID userId, List<CartItem> items) {
        this.userId = userId;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
    }

    public void addItem(CartItem item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).productId().equals(item.productId())) {
                int newQuantity = items.get(i).quantity() + item.quantity();
                items.set(i, new CartItem(
                        item.productId(),
                        item.productName(),
                        item.price(),
                        newQuantity,
                        item.imageUrl()
                ));
                return;
            }
        }
        items.add(item);
    }

    public void removeItem(UUID productId) {
        items.removeIf(item -> item.productId().equals(productId));
    }

    public void clear() {
        items.clear();
    }

    @JsonIgnore
    public BigDecimal totalPrice() {
        return items.stream()
                .map(CartItem::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return items.isEmpty();
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
    }
}
