package com.techmarket.orderservice.application.port.in;

import com.techmarket.orderservice.domain.model.Order;
import com.techmarket.orderservice.domain.model.OrderStatus;

import java.util.UUID;

public interface UpdateOrderStatusUseCase {

    Order updateStatus(UUID orderId, OrderStatus newStatus);
}
