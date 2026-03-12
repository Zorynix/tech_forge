package com.techmarket.orderservice.application.port.out;

import com.techmarket.orderservice.domain.event.OrderEvent;

public interface OrderEventPublisher {

    void publish(OrderEvent event);
}
