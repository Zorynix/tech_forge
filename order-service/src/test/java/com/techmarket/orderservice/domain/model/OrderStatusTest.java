package com.techmarket.orderservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusTest {

    @ParameterizedTest
    @CsvSource({
            "CREATED, CONFIRMED, true",
            "CREATED, CANCELLED, true",
            "CREATED, PROCESSING, false",
            "CREATED, COMPLETED, false",
            "CONFIRMED, PROCESSING, true",
            "CONFIRMED, CANCELLED, true",
            "CONFIRMED, COMPLETED, false",
            "PROCESSING, READY_FOR_PICKUP, true",
            "PROCESSING, CANCELLED, true",
            "PROCESSING, COMPLETED, false",
            "READY_FOR_PICKUP, COMPLETED, true",
            "READY_FOR_PICKUP, CANCELLED, true",
            "COMPLETED, CANCELLED, false",
            "CANCELLED, CREATED, false",
            "CANCELLED, CONFIRMED, false"
    })
    @DisplayName("should validate status transitions")
    void canTransitionTo(OrderStatus from, OrderStatus to, boolean expected) {
        assertThat(from.canTransitionTo(to)).isEqualTo(expected);
    }

    @Test
    @DisplayName("COMPLETED should not transition to anything")
    void completed_noTransitions() {
        for (OrderStatus status : OrderStatus.values()) {
            assertThat(OrderStatus.COMPLETED.canTransitionTo(status)).isFalse();
        }
    }

    @Test
    @DisplayName("CANCELLED should not transition to anything")
    void cancelled_noTransitions() {
        for (OrderStatus status : OrderStatus.values()) {
            assertThat(OrderStatus.CANCELLED.canTransitionTo(status)).isFalse();
        }
    }
}
