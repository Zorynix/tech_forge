package com.techmarket.orderservice.domain.model;

import java.util.Map;
import java.util.Set;

public enum OrderStatus {
    CREATED,
    CONFIRMED,
    PROCESSING,
    READY_FOR_PICKUP,
    COMPLETED,
    CANCELLED;

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            CREATED, Set.of(CONFIRMED, CANCELLED),
            CONFIRMED, Set.of(PROCESSING, CANCELLED),
            PROCESSING, Set.of(READY_FOR_PICKUP, CANCELLED),
            READY_FOR_PICKUP, Set.of(COMPLETED, CANCELLED),
            COMPLETED, Set.of(),
            CANCELLED, Set.of()
    );

    public boolean canTransitionTo(OrderStatus target) {
        return ALLOWED_TRANSITIONS.getOrDefault(this, Set.of()).contains(target);
    }
}
