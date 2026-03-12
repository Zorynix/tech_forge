package com.techmarket.productservice.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VideoReviewJpaRepository extends JpaRepository<VideoReviewJpaEntity, UUID> {

    List<VideoReviewJpaEntity> findByProductId(UUID productId);
}
