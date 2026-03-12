package com.techmarket.productservice.application.port.out;

import com.techmarket.productservice.domain.model.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductCachePort {

    Optional<Product> get(UUID productId);

    void put(Product product);

    void evict(UUID productId);
}
