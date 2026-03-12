package com.techmarket.productservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private UUID categoryId;
    private List<String> imageUrls;
    private List<String> spinImages;
    private String modelUrl;
    private Map<String, String> specifications;
    private int stockQuantity;
    private boolean active;
    private Double averageRating;
    private int reviewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
