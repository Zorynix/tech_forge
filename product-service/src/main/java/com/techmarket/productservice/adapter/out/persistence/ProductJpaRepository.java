package com.techmarket.productservice.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {

    @Query("SELECT p FROM ProductJpaEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<ProductJpaEntity> searchByName(@Param("query") String query, Pageable pageable);

    Page<ProductJpaEntity> findByCategoryId(UUID categoryId, Pageable pageable);

    Page<ProductJpaEntity> findByActiveTrue(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE ProductJpaEntity p SET p.averageRating = :rating, p.reviewCount = :count WHERE p.id = :id")
    void updateRating(@Param("id") UUID id, @Param("rating") double rating, @Param("count") int count);
}
