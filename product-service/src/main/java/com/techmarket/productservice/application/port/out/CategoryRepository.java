package com.techmarket.productservice.application.port.out;

import com.techmarket.productservice.domain.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {

    List<Category> findAll();

    Optional<Category> findById(UUID id);

    Optional<Category> findBySlug(String slug);

    Category save(Category category);
}
