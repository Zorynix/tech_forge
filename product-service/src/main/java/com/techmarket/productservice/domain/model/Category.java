package com.techmarket.productservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Category {

    private final UUID id;
    private final String name;
    private final String slug;
    private final String description;
    private final UUID parentCategoryId;
    private final String imageUrl;
}
