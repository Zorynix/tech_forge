package com.techmarket.orderservice.adapter.out.persistence;

import com.techmarket.orderservice.domain.model.*;
import com.techmarket.orderservice.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderPersistenceAdapterIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private OrderPersistenceAdapter adapter;

    private Order createOrder(UUID userId) {
        return new Order(
                UUID.randomUUID(),
                userId,
                List.of(new OrderItem(UUID.randomUUID(), "iPhone 17 Pro", BigDecimal.valueOf(129990), 1, "img.jpg")),
                OrderStatus.CREATED,
                BigDecimal.valueOf(129990),
                "+79001234567",
                "test@example.com",
                Instant.now(),
                Instant.now()
        );
    }

    @Test
    @DisplayName("should save and find order by id")
    void saveAndFindById() {
        UUID userId = UUID.randomUUID();
        Order order = createOrder(userId);
        Order saved = adapter.save(order);

        Optional<Order> found = adapter.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo(userId);
        assertThat(found.get().getItems()).hasSize(1);
        assertThat(found.get().getStatus()).isEqualTo(OrderStatus.CREATED);
    }

    @Test
    @DisplayName("should find orders by userId with pagination")
    void findByUserId() {
        UUID userId = UUID.randomUUID();
        adapter.save(createOrder(userId));
        adapter.save(createOrder(userId));

        Page<Order> page = adapter.findByUserId(userId, PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("should return empty for non-existent order")
    void findById_notFound() {
        Optional<Order> found = adapter.findById(UUID.randomUUID());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("should update order status")
    void updateOrderStatus() {
        UUID userId = UUID.randomUUID();
        Order order = adapter.save(createOrder(userId));

        order.setStatus(OrderStatus.CONFIRMED);
        adapter.save(order);

        Optional<Order> updated = adapter.findById(order.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }
}
