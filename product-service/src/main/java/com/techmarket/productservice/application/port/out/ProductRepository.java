package com.techmarket.productservice.application.port.out;

import com.techmarket.productservice.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Optional<Product> findById(UUID id);

    Page<Product> findAll(Pageable pageable);

    Page<Product> searchByName(String query, Pageable pageable);

    Page<Product> findByCategoryId(UUID categoryId, Pageable pageable);

    Product save(Product product);

    void updateRating(UUID productId, double averageRating, int reviewCount);
}
