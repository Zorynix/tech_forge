package com.techmarket.orderservice.application.port.out;

import com.techmarket.orderservice.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Optional<Order> findById(UUID id);

    Page<Order> findByUserId(UUID userId, Pageable pageable);

    Order save(Order order);
}
