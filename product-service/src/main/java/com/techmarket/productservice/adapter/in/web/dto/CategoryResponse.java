package com.techmarket.productservice.adapter.in.web.dto;

import com.techmarket.productservice.domain.model.Category;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        String slug,
        String description,
        UUID parentCategoryId,
        String imageUrl
) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getParentCategoryId(),
                category.getImageUrl()
        );
    }
}
