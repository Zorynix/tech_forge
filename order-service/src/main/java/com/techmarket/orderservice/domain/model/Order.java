package com.techmarket.orderservice.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Order {

    private UUID id;
    private UUID userId;
    private List<OrderItem> items;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private String phone;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;

    public Order() {
    }

    public Order(UUID id, UUID userId, List<OrderItem> items, OrderStatus status,
                 BigDecimal totalPrice, String phone, String email, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.status = status;
        this.totalPrice = totalPrice;
        this.phone = phone;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Order createFromCart(UUID userId, Cart cart, String phone, String email) {
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(OrderItem::fromCartItem)
                .toList();

        Instant now = Instant.now();
        return new Order(
                UUID.randomUUID(),
                userId,
                orderItems,
                OrderStatus.CREATED,
                cart.totalPrice(),
                phone,
                email,
                now,
                now
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
