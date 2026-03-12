package com.techmarket.productservice.adapter.in.web.dto;

import com.techmarket.productservice.domain.model.ReviewSummary;

import java.util.UUID;

public record ReviewSummaryResponse(
        UUID productId,
        double averageQuality,
        double averagePrice,
        double averageDesign,
        double averageFeatures,
        double averageUsability,
        double averageOverall,
        int totalReviews
) {
    public static ReviewSummaryResponse from(ReviewSummary summary) {
        return new ReviewSummaryResponse(
                summary.getProductId(),
                summary.getAverageQuality(),
                summary.getAveragePrice(),
                summary.getAverageDesign(),
                summary.getAverageFeatures(),
                summary.getAverageUsability(),
                summary.getAverageOverall(),
                summary.getTotalReviews()
        );
    }
}
