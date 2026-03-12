package com.techmarket.productservice.adapter.in.web.dto;

import jakarta.validation.constraints.*;

public record CreateReviewRequest(
        @NotBlank(message = "Author name is required")
        @Size(max = 255, message = "Author name must be at most 255 characters")
        String authorName,

        @Size(max = 500, message = "Title must be at most 500 characters")
        String title,

        @NotBlank(message = "Content is required")
        @Size(max = 5000, message = "Content must be at most 5000 characters")
        String content,

        @Min(value = 1, message = "Rating must be between 1 and 10")
        @Max(value = 10, message = "Rating must be between 1 and 10")
        int ratingQuality,

        @Min(value = 1, message = "Rating must be between 1 and 10")
        @Max(value = 10, message = "Rating must be between 1 and 10")
        int ratingPrice,

        @Min(value = 1, message = "Rating must be between 1 and 10")
        @Max(value = 10, message = "Rating must be between 1 and 10")
        int ratingDesign,

        @Min(value = 1, message = "Rating must be between 1 and 10")
        @Max(value = 10, message = "Rating must be between 1 and 10")
        int ratingFeatures,

        @Min(value = 1, message = "Rating must be between 1 and 10")
        @Max(value = 10, message = "Rating must be between 1 and 10")
        int ratingUsability
) {}
