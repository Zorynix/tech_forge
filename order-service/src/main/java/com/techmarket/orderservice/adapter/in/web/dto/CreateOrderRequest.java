package com.techmarket.orderservice.adapter.in.web.dto;

public record CreateOrderRequest(
        String phone,
        String email
) {
}
