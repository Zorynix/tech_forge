package com.techmarket.productservice.application.port.in;

import com.techmarket.productservice.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GetProductUseCase {

    Product getById(UUID id);

    Page<Product> getAll(Pageable pageable);

    Page<Product> searchByName(String query, Pageable pageable);

    Page<Product> getByCategory(UUID categoryId, Pageable pageable);
}
