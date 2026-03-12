package com.techmarket.productservice.adapter.in.web.dto;

import com.techmarket.productservice.domain.model.Review;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        UUID productId,
        String authorName,
        String title,
        String content,
        int ratingQuality,
        int ratingPrice,
        int ratingDesign,
        int ratingFeatures,
        int ratingUsability,
        double overallRating,
        LocalDateTime createdAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getProductId(),
                review.getAuthorName(),
                review.getTitle(),
                review.getContent(),
                review.getRatingQuality(),
                review.getRatingPrice(),
                review.getRatingDesign(),
                review.getRatingFeatures(),
                review.getRatingUsability(),
                review.getOverallRating(),
                review.getCreatedAt()
        );
    }
}
