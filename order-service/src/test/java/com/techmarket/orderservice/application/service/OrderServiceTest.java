package com.techmarket.orderservice.application.service;

import com.techmarket.orderservice.application.port.out.CartStore;
import com.techmarket.orderservice.application.port.out.OrderEventPublisher;
import com.techmarket.orderservice.application.port.out.OrderRepository;
import com.techmarket.orderservice.domain.exception.EmptyCartException;
import com.techmarket.orderservice.domain.exception.InvalidOrderStatusTransitionException;
import com.techmarket.orderservice.domain.exception.OrderNotFoundException;
import com.techmarket.orderservice.domain.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartStore cartStore;
    @Mock
    private OrderEventPublisher eventPublisher;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void initTransactionSync() {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.initSynchronization();
        }
    }

    @AfterEach
    void clearTransactionSync() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    private Order createOrder(UUID id, UUID userId, OrderStatus status) {
        return new Order(id, userId,
                List.of(new OrderItem(UUID.randomUUID(), "iPhone", BigDecimal.valueOf(999), 1, "img.jpg")),
                status, BigDecimal.valueOf(999), "+79001234567", "test@example.com",
                Instant.now(), Instant.now());
    }

    @Nested
    @DisplayName("createOrder")
    class CreateOrder {

        @Test
        @DisplayName("should throw when cart is null")
        void createOrder_nullCart_throws() {
            UUID userId = UUID.randomUUID();
            when(cartStore.get(userId)).thenReturn(null);

            assertThatThrownBy(() -> orderService.createOrder(userId, "+79001234567", "test@example.com"))
                    .isInstanceOf(EmptyCartException.class);
        }

        @Test
        @DisplayName("should throw when cart is empty")
        void createOrder_emptyCart_throws() {
            UUID userId = UUID.randomUUID();
            when(cartStore.get(userId)).thenReturn(new Cart(userId, null));

            assertThatThrownBy(() -> orderService.createOrder(userId, "+79001234567", "test@example.com"))
                    .isInstanceOf(EmptyCartException.class);
        }

        @Test
        @DisplayName("should save order from cart")
        void createOrder_validCart_savesOrder() {
            UUID userId = UUID.randomUUID();
            Cart cart = new Cart(userId, null);
            cart.addItem(new CartItem(UUID.randomUUID(), "iPhone", BigDecimal.valueOf(999), 1, "img.jpg"));
            when(cartStore.get(userId)).thenReturn(cart);

            Order savedOrder = createOrder(UUID.randomUUID(), userId, OrderStatus.CREATED);
            when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

            Order result = orderService.createOrder(userId, "+79001234567", "test@example.com");

            assertThat(result.getStatus()).isEqualTo(OrderStatus.CREATED);
            verify(orderRepository).save(any(Order.class));
        }
    }

    @Nested
    @DisplayName("getById")
    class GetById {

        @Test
        @DisplayName("should return order when found")
        void getById_found_returnsOrder() {
            UUID orderId = UUID.randomUUID();
            Order order = createOrder(orderId, UUID.randomUUID(), OrderStatus.CREATED);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            Order result = orderService.getById(orderId);

            assertThat(result.getId()).isEqualTo(orderId);
        }

        @Test
        @DisplayName("should throw when not found")
        void getById_notFound_throws() {
            UUID orderId = UUID.randomUUID();
            when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.getById(orderId))
                    .isInstanceOf(OrderNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getByUserId")
    class GetByUserId {

        @Test
        @DisplayName("should return paginated orders")
        void getByUserId_returnsPaginatedOrders() {
            UUID userId = UUID.randomUUID();
            Page<Order> page = new PageImpl<>(List.of(createOrder(UUID.randomUUID(), userId, OrderStatus.CREATED)));
            when(orderRepository.findByUserId(userId, PageRequest.of(0, 20))).thenReturn(page);

            Page<Order> result = orderService.getByUserId(userId, PageRequest.of(0, 20));

            assertThat(result.getTotalElements()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("updateStatus")
    class UpdateStatus {

        @Test
        @DisplayName("should update status for valid transition")
        void updateStatus_validTransition_updatesStatus() {
            UUID orderId = UUID.randomUUID();
            Order order = createOrder(orderId, UUID.randomUUID(), OrderStatus.CREATED);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderRepository.save(order)).thenReturn(order);

            Order result = orderService.updateStatus(orderId, OrderStatus.CONFIRMED);

            assertThat(result.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        }

        @Test
        @DisplayName("should throw for invalid transition")
        void updateStatus_invalidTransition_throws() {
            UUID orderId = UUID.randomUUID();
            Order order = createOrder(orderId, UUID.randomUUID(), OrderStatus.CREATED);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            assertThatThrownBy(() -> orderService.updateStatus(orderId, OrderStatus.COMPLETED))
                    .isInstanceOf(InvalidOrderStatusTransitionException.class);
        }

        @Test
        @DisplayName("should throw when order not found")
        void updateStatus_notFound_throws() {
            UUID orderId = UUID.randomUUID();
            when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.updateStatus(orderId, OrderStatus.CONFIRMED))
                    .isInstanceOf(OrderNotFoundException.class);
        }
    }
}
