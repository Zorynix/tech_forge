package com.techmarket.productservice.application.port.in;

import com.techmarket.productservice.domain.model.Review;

import java.util.UUID;

public interface CreateReviewUseCase {

    Review create(UUID productId, UUID authorId, String authorName, String title, String content,
                  int ratingQuality, int ratingPrice, int ratingDesign, int ratingFeatures, int ratingUsability);
}
