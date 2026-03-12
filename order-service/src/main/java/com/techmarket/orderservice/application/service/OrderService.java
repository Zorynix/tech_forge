package com.techmarket.orderservice.application.service;

import com.techmarket.orderservice.application.port.in.CreateOrderUseCase;
import com.techmarket.orderservice.application.port.in.GetOrderUseCase;
import com.techmarket.orderservice.application.port.in.UpdateOrderStatusUseCase;
import com.techmarket.orderservice.application.port.out.CartStore;
import com.techmarket.orderservice.application.port.out.OrderEventPublisher;
import com.techmarket.orderservice.application.port.out.OrderRepository;
import com.techmarket.orderservice.domain.event.OrderEvent;
import com.techmarket.orderservice.domain.exception.EmptyCartException;
import com.techmarket.orderservice.domain.exception.InvalidOrderStatusTransitionException;
import com.techmarket.orderservice.domain.exception.OrderNotFoundException;
import com.techmarket.orderservice.domain.model.Cart;
import com.techmarket.orderservice.domain.model.Order;
import com.techmarket.orderservice.domain.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService implements CreateOrderUseCase, GetOrderUseCase, UpdateOrderStatusUseCase {

    private final OrderRepository orderRepository;
    private final CartStore cartStore;
    private final OrderEventPublisher eventPublisher;

    private static final Map<OrderStatus, String> STATUS_MESSAGES = Map.of(
            OrderStatus.CREATED, "Your order has been created and is awaiting confirmation.",
            OrderStatus.CONFIRMED, "Your order has been confirmed.",
            OrderStatus.PROCESSING, "Your order is being processed.",
            OrderStatus.READY_FOR_PICKUP, "Your order is ready for pickup!",
            OrderStatus.COMPLETED, "Your order has been completed. Thank you!",
            OrderStatus.CANCELLED, "Your order has been cancelled."
    );

    @Override
    @Transactional
    public Order createOrder(UUID userId, String phone, String email) {
        Cart cart = cartStore.get(userId);
        if (cart == null || cart.isEmpty()) {
            throw new EmptyCartException();
        }

        Order order = Order.createFromCart(userId, cart, phone, email);
        Order savedOrder = orderRepository.save(order);
        log.info("Order saved: orderId={}, userId={}", savedOrder.getId(), userId);

        // Publish event and delete cart AFTER transaction commits successfully
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    cartStore.delete(userId);
                } catch (Exception e) {
                    log.warn("Failed to delete cart after order creation: userId={}", userId, e);
                }
                try {
                    eventPublisher.publish(OrderEvent.of(
                            savedOrder.getId(),
                            savedOrder.getUserId(),
                            OrderStatus.CREATED.name(),
                            savedOrder.getPhone(),
                            STATUS_MESSAGES.get(OrderStatus.CREATED)
                    ));
                } catch (Exception e) {
                    log.error("Failed to publish order event: orderId={}", savedOrder.getId(), e);
                }
            }
        });

        return savedOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public Order getById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> getByUserId(UUID userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    @Override
    @Transactional
    public Order updateStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.getStatus().canTransitionTo(newStatus)) {
            throw new InvalidOrderStatusTransitionException(order.getStatus(), newStatus);
        }

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    eventPublisher.publish(OrderEvent.of(
                            updatedOrder.getId(),
                            updatedOrder.getUserId(),
                            newStatus.name(),
                            updatedOrder.getPhone(),
                            STATUS_MESSAGES.get(newStatus)
                    ));
                } catch (Exception e) {
                    log.error("Failed to publish order status event: orderId={}", updatedOrder.getId(), e);
                }
            }
        });

        return updatedOrder;
    }
}
