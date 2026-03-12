package com.techmarket.notificationservice.application.port.in;

import com.techmarket.notificationservice.domain.event.OrderEvent;

public interface ProcessOrderEventUseCase {

    void process(OrderEvent event);
}
