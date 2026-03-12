package com.techmarket.productservice.adapter.in.web;

import com.techmarket.productservice.adapter.in.web.dto.CreateReviewRequest;
import com.techmarket.productservice.adapter.in.web.dto.ReviewResponse;
import com.techmarket.productservice.adapter.in.web.dto.ReviewSummaryResponse;
import com.techmarket.productservice.application.port.in.CreateReviewUseCase;
import com.techmarket.productservice.application.port.in.GetReviewUseCase;
import com.techmarket.productservice.infrastructure.security.JwtTokenParser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products/{productId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final GetReviewUseCase getReviewUseCase;
    private final CreateReviewUseCase createReviewUseCase;
    private final JwtTokenParser jwtTokenParser;

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getByProductId(@PathVariable UUID productId) {
        var reviews = getReviewUseCase.getByProductId(productId).stream()
                .map(ReviewResponse::from)
                .toList();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/summary")
    public ResponseEntity<ReviewSummaryResponse> getSummary(@PathVariable UUID productId) {
        var summary = getReviewUseCase.getSummaryByProductId(productId);
        return ResponseEntity.ok(ReviewSummaryResponse.from(summary));
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> create(
            @PathVariable UUID productId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @Valid @RequestBody CreateReviewRequest request) {

        UUID userId = jwtTokenParser.extractFromHeader(authHeader)
                .orElse(null);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var review = createReviewUseCase.create(
                productId,
                userId,
                request.authorName(),
                request.title(),
                request.content(),
                request.ratingQuality(),
                request.ratingPrice(),
                request.ratingDesign(),
                request.ratingFeatures(),
                request.ratingUsability()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(ReviewResponse.from(review));
    }
}
