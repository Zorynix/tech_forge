package com.techmarket.productservice.application.service;

import com.techmarket.productservice.application.port.in.GetVideoReviewUseCase;
import com.techmarket.productservice.application.port.out.VideoReviewRepository;
import com.techmarket.productservice.domain.model.VideoReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoReviewService implements GetVideoReviewUseCase {

    private final VideoReviewRepository videoReviewRepository;

    @Override
    public List<VideoReview> getByProductId(UUID productId) {
        return videoReviewRepository.findByProductId(productId);
    }
}
