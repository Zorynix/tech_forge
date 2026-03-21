package com.techmarket.productservice.application.service;

import com.techmarket.productservice.application.port.out.ProductCachePort;
import com.techmarket.productservice.application.port.out.ProductRepository;
import com.techmarket.productservice.application.port.out.ReviewRepository;
import com.techmarket.productservice.domain.model.Review;
import com.techmarket.productservice.domain.model.ReviewSummary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductCachePort productCache;

    @InjectMocks
    private ReviewService reviewService;

    private Review createReview(UUID productId, int quality, int price, int design, int features, int usability) {
        return Review.builder()
                .id(UUID.randomUUID())
                .productId(productId)
                .authorId(UUID.randomUUID())
                .authorName("Reviewer")
                .title("Great product")
                .content("Loved it")
                .ratingQuality(quality)
                .ratingPrice(price)
                .ratingDesign(design)
                .ratingFeatures(features)
                .ratingUsability(usability)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("getByProductId should return reviews")
    void getByProductId_returnsReviews() {
        UUID productId = UUID.randomUUID();
        List<Review> reviews = List.of(createReview(productId, 5, 4, 5, 4, 5));
        when(reviewRepository.findByProductId(productId)).thenReturn(reviews);

        List<Review> result = reviewService.getByProductId(productId);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("getSummaryByProductId should compute summary")
    void getSummaryByProductId_computesSummary() {
        UUID productId = UUID.randomUUID();
        List<Review> reviews = List.of(
                createReview(productId, 5, 5, 5, 5, 5),
                createReview(productId, 3, 3, 3, 3, 3)
        );
        when(reviewRepository.findByProductId(productId)).thenReturn(reviews);

        ReviewSummary summary = reviewService.getSummaryByProductId(productId);

        assertThat(summary.getTotalReviews()).isEqualTo(2);
        assertThat(summary.getAverageOverall()).isBetween(3.0, 5.0);
    }

    @Test
    @DisplayName("create should save review and update product rating")
    void create_savesReviewAndUpdatesRating() {
        UUID productId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        Review savedReview = createReview(productId, 5, 4, 5, 4, 5);

        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);
        when(reviewRepository.findByProductId(productId)).thenReturn(List.of(savedReview));

        Review result = reviewService.create(productId, authorId, "Author", "Title", "Content", 5, 4, 5, 4, 5);

        assertThat(result).isNotNull();
        verify(productRepository).updateRating(eq(productId), anyDouble(), eq(1));
        verify(productCache).evict(productId);
    }
}
