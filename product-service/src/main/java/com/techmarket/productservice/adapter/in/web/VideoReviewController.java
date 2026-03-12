package com.techmarket.productservice.adapter.in.web;

import com.techmarket.productservice.adapter.in.web.dto.VideoReviewResponse;
import com.techmarket.productservice.application.port.in.GetVideoReviewUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products/{productId}/videos")
@RequiredArgsConstructor
public class VideoReviewController {

    private final GetVideoReviewUseCase getVideoReviewUseCase;

    @GetMapping
    public ResponseEntity<List<VideoReviewResponse>> getByProductId(@PathVariable UUID productId) {
        var reviews = getVideoReviewUseCase.getByProductId(productId).stream()
                .map(VideoReviewResponse::from)
                .toList();
        return ResponseEntity.ok(reviews);
    }
}
