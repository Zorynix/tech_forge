package com.techmarket.productservice.adapter.out.persistence;

import com.techmarket.productservice.application.port.out.ReviewRepository;
import com.techmarket.productservice.domain.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReviewPersistenceAdapter implements ReviewRepository {

    private final ReviewJpaRepository jpaRepository;
    private final ProductMapper mapper;

    @Override
    public List<Review> findByProductId(UUID productId) {
        return jpaRepository.findByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Review save(Review review) {
        var entity = mapper.toJpaEntity(review);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
