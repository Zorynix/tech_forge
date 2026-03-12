package com.techmarket.productservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class VideoReview {

    private final UUID id;
    private final UUID productId;
    private final String title;
    private final String videoUrl;
    private final String thumbnailUrl;
    private final int durationSeconds;
    private final LocalDateTime createdAt;
}
