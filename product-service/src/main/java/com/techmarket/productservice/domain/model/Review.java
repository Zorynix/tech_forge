package com.techmarket.productservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Review {

    private final UUID id;
    private final UUID productId;
    private final String authorName;
    private final UUID authorId;
    private final String title;
    private final String content;
    private final int ratingQuality;
    private final int ratingPrice;
    private final int ratingDesign;
    private final int ratingFeatures;
    private final int ratingUsability;
    private final LocalDateTime createdAt;

    public double getOverallRating() {
        return (ratingQuality + ratingPrice + ratingDesign + ratingFeatures + ratingUsability) / 5.0;
    }
}
