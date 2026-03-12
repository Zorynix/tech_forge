package com.techmarket.productservice.application.port.in;

import com.techmarket.productservice.domain.model.VideoReview;

import java.util.List;
import java.util.UUID;

public interface GetVideoReviewUseCase {

    List<VideoReview> getByProductId(UUID productId);
}
