package com.techmarket.productservice.adapter.in.web.dto;

import com.techmarket.productservice.domain.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        UUID categoryId,
        List<String> imageUrls,
        List<String> spinImages,
        String modelUrl,
        Map<String, String> specifications,
        int stockQuantity,
        boolean active,
        Double averageRating,
        int reviewCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategoryId(),
                product.getImageUrls(),
                product.getSpinImages(),
                product.getModelUrl(),
                product.getSpecifications(),
                product.getStockQuantity(),
                product.isActive(),
                product.getAverageRating(),
                product.getReviewCount(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
