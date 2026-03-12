package com.techmarket.orderservice.application.port.in;

import com.techmarket.orderservice.domain.model.Order;

import java.util.UUID;

public interface CreateOrderUseCase {

    Order createOrder(UUID userId, String phone, String email);
}
