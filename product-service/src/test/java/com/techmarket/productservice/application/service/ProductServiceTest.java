package com.techmarket.productservice.application.service;

import com.techmarket.productservice.application.port.out.ProductCachePort;
import com.techmarket.productservice.application.port.out.ProductRepository;
import com.techmarket.productservice.domain.exception.ResourceNotFoundException;
import com.techmarket.productservice.domain.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductCachePort productCache;

    @InjectMocks
    private ProductService productService;

    private Product createProduct(UUID id) {
        return Product.builder()
                .id(id)
                .name("iPhone 17 Pro")
                .description("Latest iPhone")
                .price(BigDecimal.valueOf(129990))
                .stockQuantity(50)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("getById should return cached product if present")
    void getById_cached_returnsCachedProduct() {
        UUID id = UUID.randomUUID();
        Product product = createProduct(id);
        when(productCache.get(id)).thenReturn(Optional.of(product));

        Product result = productService.getById(id);

        assertThat(result.getId()).isEqualTo(id);
        verifyNoInteractions(productRepository);
    }

    @Test
    @DisplayName("getById should fetch from DB and cache on miss")
    void getById_cacheMiss_fetchesFromDbAndCaches() {
        UUID id = UUID.randomUUID();
        Product product = createProduct(id);
        when(productCache.get(id)).thenReturn(Optional.empty());
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product result = productService.getById(id);

        assertThat(result.getId()).isEqualTo(id);
        verify(productCache).put(product);
    }

    @Test
    @DisplayName("getById should throw when product not found")
    void getById_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(productCache.get(id)).thenReturn(Optional.empty());
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("getAll should return paginated products")
    void getAll_returnsPaginatedProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(createProduct(UUID.randomUUID())));
        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<Product> result = productService.getAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("searchByName should delegate to repository")
    void searchByName_delegatesToRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(createProduct(UUID.randomUUID())));
        when(productRepository.searchByName("iPhone", pageable)).thenReturn(page);

        Page<Product> result = productService.searchByName("iPhone", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("getByCategory should delegate to repository")
    void getByCategory_delegatesToRepository() {
        UUID categoryId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(createProduct(UUID.randomUUID())));
        when(productRepository.findByCategoryId(categoryId, pageable)).thenReturn(page);

        Page<Product> result = productService.getByCategory(categoryId, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}
