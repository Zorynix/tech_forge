package com.techmarket.productservice.application.service;

import com.techmarket.productservice.application.port.in.GetCategoryUseCase;
import com.techmarket.productservice.application.port.out.CategoryRepository;
import com.techmarket.productservice.domain.exception.ResourceNotFoundException;
import com.techmarket.productservice.domain.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService implements GetCategoryUseCase {

    private final CategoryRepository categoryRepository;

    @Override
    @Cacheable(value = "categories", key = "'all'")
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Cacheable(value = "categories", key = "#id")
    public Category getById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }
}
