package com.techmarket.productservice.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ReviewSummary {

    private final UUID productId;
    private final double averageQuality;
    private final double averagePrice;
    private final double averageDesign;
    private final double averageFeatures;
    private final double averageUsability;
    private final double averageOverall;
    private final int totalReviews;

    public static ReviewSummary from(UUID productId, List<Review> reviews) {
        if (reviews.isEmpty()) {
            return empty(productId);
        }
        double quality = reviews.stream().mapToInt(Review::getRatingQuality).average().orElse(0);
        double price = reviews.stream().mapToInt(Review::getRatingPrice).average().orElse(0);
        double design = reviews.stream().mapToInt(Review::getRatingDesign).average().orElse(0);
        double features = reviews.stream().mapToInt(Review::getRatingFeatures).average().orElse(0);
        double usability = reviews.stream().mapToInt(Review::getRatingUsability).average().orElse(0);
        double overall = (quality + price + design + features + usability) / 5.0;

        return ReviewSummary.builder()
                .productId(productId)
                .averageQuality(Math.round(quality * 10) / 10.0)
                .averagePrice(Math.round(price * 10) / 10.0)
                .averageDesign(Math.round(design * 10) / 10.0)
                .averageFeatures(Math.round(features * 10) / 10.0)
                .averageUsability(Math.round(usability * 10) / 10.0)
                .averageOverall(Math.round(overall * 10) / 10.0)
                .totalReviews(reviews.size())
                .build();
    }

    public static ReviewSummary empty(UUID productId) {
        return ReviewSummary.builder()
                .productId(productId)
                .averageQuality(0)
                .averagePrice(0)
                .averageDesign(0)
                .averageFeatures(0)
                .averageUsability(0)
                .averageOverall(0)
                .totalReviews(0)
                .build();
    }
}
