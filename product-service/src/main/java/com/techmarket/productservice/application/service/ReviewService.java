package com.techmarket.productservice.application.service;

import com.techmarket.productservice.application.port.in.CreateReviewUseCase;
import com.techmarket.productservice.application.port.in.GetReviewUseCase;
import com.techmarket.productservice.application.port.out.ProductCachePort;
import com.techmarket.productservice.application.port.out.ProductRepository;
import com.techmarket.productservice.application.port.out.ReviewRepository;
import com.techmarket.productservice.domain.model.Review;
import com.techmarket.productservice.domain.model.ReviewSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService implements GetReviewUseCase, CreateReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ProductCachePort productCache;

    @Override
    @Transactional(readOnly = true)
    public List<Review> getByProductId(UUID productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewSummary getSummaryByProductId(UUID productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        return ReviewSummary.from(productId, reviews);
    }

    @Override
    @Transactional
    public Review create(UUID productId, UUID authorId, String authorName, String title, String content,
                         int ratingQuality, int ratingPrice, int ratingDesign, int ratingFeatures, int ratingUsability) {
        Review review = Review.builder()
                .productId(productId)
                .authorId(authorId)
                .authorName(authorName)
                .title(title)
                .content(content)
                .ratingQuality(ratingQuality)
                .ratingPrice(ratingPrice)
                .ratingDesign(ratingDesign)
                .ratingFeatures(ratingFeatures)
                .ratingUsability(ratingUsability)
                .build();

        Review saved = reviewRepository.save(review);

        // Recalculate product rating
        List<Review> allReviews = reviewRepository.findByProductId(productId);
        double avgRating = allReviews.stream()
                .mapToDouble(Review::getOverallRating)
                .average()
                .orElse(0);
        productRepository.updateRating(productId, Math.round(avgRating * 10) / 10.0, allReviews.size());
        productCache.evict(productId);

        return saved;
    }
}
