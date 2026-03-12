package com.techmarket.productservice.application.port.out;

import com.techmarket.productservice.domain.model.Review;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository {

    List<Review> findByProductId(UUID productId);

    Review save(Review review);
}
