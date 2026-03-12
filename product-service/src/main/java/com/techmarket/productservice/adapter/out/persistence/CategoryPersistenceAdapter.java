package com.techmarket.productservice.adapter.out.persistence;

import com.techmarket.productservice.application.port.out.CategoryRepository;
import com.techmarket.productservice.domain.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryRepository {

    private final CategoryJpaRepository jpaRepository;
    private final ProductMapper mapper;

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        return jpaRepository.findBySlug(slug).map(mapper::toDomain);
    }

    @Override
    public Category save(Category category) {
        var entity = mapper.toJpaEntity(category);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
