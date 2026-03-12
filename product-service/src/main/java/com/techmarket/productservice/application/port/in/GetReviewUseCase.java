package com.techmarket.productservice.application.port.in;

import com.techmarket.productservice.domain.model.Review;
import com.techmarket.productservice.domain.model.ReviewSummary;

import java.util.List;
import java.util.UUID;

public interface GetReviewUseCase {

    List<Review> getByProductId(UUID productId);

    ReviewSummary getSummaryByProductId(UUID productId);
}
