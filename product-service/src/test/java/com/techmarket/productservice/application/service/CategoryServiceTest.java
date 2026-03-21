package com.techmarket.productservice.application.service;

import com.techmarket.productservice.application.port.out.CategoryRepository;
import com.techmarket.productservice.domain.exception.ResourceNotFoundException;
import com.techmarket.productservice.domain.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName("getAll should return all categories")
    void getAll_returnsAllCategories() {
        Category cat1 = Category.builder().id(UUID.randomUUID()).name("Smartphones").slug("smartphones").build();
        Category cat2 = Category.builder().id(UUID.randomUUID()).name("Laptops").slug("laptops").build();
        when(categoryRepository.findAll()).thenReturn(List.of(cat1, cat2));

        List<Category> result = categoryService.getAll();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("getById should return category when found")
    void getById_found_returnsCategory() {
        UUID id = UUID.randomUUID();
        Category category = Category.builder().id(id).name("Smartphones").slug("smartphones").build();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        Category result = categoryService.getById(id);

        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("getById should throw when not found")
    void getById_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
