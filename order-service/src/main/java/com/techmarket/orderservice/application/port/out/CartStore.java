package com.techmarket.orderservice.application.port.out;

import com.techmarket.orderservice.domain.model.Cart;

import java.util.UUID;

public interface CartStore {

    Cart get(UUID userId);

    void save(UUID userId, Cart cart);

    void delete(UUID userId);
}
