package com.techmarket.productservice.application.port.out;

import com.techmarket.productservice.domain.model.VideoReview;

import java.util.List;
import java.util.UUID;

public interface VideoReviewRepository {

    List<VideoReview> findByProductId(UUID productId);

    VideoReview save(VideoReview videoReview);
}
