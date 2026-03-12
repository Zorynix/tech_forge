package com.techmarket.orderservice.application.port.in;

import com.techmarket.orderservice.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GetOrderUseCase {

    Order getById(UUID orderId);

    Page<Order> getByUserId(UUID userId, Pageable pageable);
}
