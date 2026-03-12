package com.techmarket.productservice.adapter.out.persistence;

import com.techmarket.productservice.application.port.out.ProductRepository;
import com.techmarket.productservice.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductRepository {

    private final ProductJpaRepository jpaRepository;
    private final ProductMapper mapper;

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return jpaRepository.findByActiveTrue(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Product> searchByName(String query, Pageable pageable) {
        return jpaRepository.searchByName(query, pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Product> findByCategoryId(UUID categoryId, Pageable pageable) {
        return jpaRepository.findByCategoryId(categoryId, pageable).map(mapper::toDomain);
    }

    @Override
    public Product save(Product product) {
        var entity = mapper.toJpaEntity(product);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void updateRating(UUID productId, double averageRating, int reviewCount) {
        jpaRepository.updateRating(productId, averageRating, reviewCount);
    }
}
