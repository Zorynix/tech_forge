package com.techmarket.productservice.adapter.in.web.dto;

import com.techmarket.productservice.domain.model.VideoReview;

import java.time.LocalDateTime;
import java.util.UUID;

public record VideoReviewResponse(
        UUID id,
        UUID productId,
        String title,
        String videoUrl,
        String thumbnailUrl,
        int durationSeconds,
        LocalDateTime createdAt
) {

    public static VideoReviewResponse from(VideoReview review) {
        return new VideoReviewResponse(
                review.getId(),
                review.getProductId(),
                review.getTitle(),
                review.getVideoUrl(),
                review.getThumbnailUrl(),
                review.getDurationSeconds(),
                review.getCreatedAt()
        );
    }
}
