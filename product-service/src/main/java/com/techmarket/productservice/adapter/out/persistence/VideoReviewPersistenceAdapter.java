package com.techmarket.productservice.adapter.out.persistence;

import com.techmarket.productservice.application.port.out.VideoReviewRepository;
import com.techmarket.productservice.domain.model.VideoReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VideoReviewPersistenceAdapter implements VideoReviewRepository {

    private final VideoReviewJpaRepository jpaRepository;
    private final ProductMapper mapper;

    @Override
    public List<VideoReview> findByProductId(UUID productId) {
        return jpaRepository.findByProductId(productId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public VideoReview save(VideoReview videoReview) {
        var entity = mapper.toJpaEntity(videoReview);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
