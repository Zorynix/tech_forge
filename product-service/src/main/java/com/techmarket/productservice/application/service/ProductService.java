package com.techmarket.productservice.application.service;

import com.techmarket.productservice.application.port.in.GetProductUseCase;
import com.techmarket.productservice.application.port.out.ProductCachePort;
import com.techmarket.productservice.application.port.out.ProductRepository;
import com.techmarket.productservice.domain.exception.ResourceNotFoundException;
import com.techmarket.productservice.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService implements GetProductUseCase {

    private final ProductRepository productRepository;
    private final ProductCachePort productCache;

    @Override
    public Product getById(UUID id) {
        return productCache.get(id)
                .orElseGet(() -> {
                    Product product = productRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Product", id));
                    productCache.put(product);
                    return product;
                });
    }

    @Override
    public Page<Product> getAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> searchByName(String query, Pageable pageable) {
        return productRepository.searchByName(query, pageable);
    }

    @Override
    public Page<Product> getByCategory(UUID categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }
}
