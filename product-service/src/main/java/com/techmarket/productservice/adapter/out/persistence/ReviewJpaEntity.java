package com.techmarket.productservice.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews", schema = "product_service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewJpaEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(name = "author_id")
    private UUID authorId;

    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "rating_quality", nullable = false)
    private int ratingQuality;

    @Column(name = "rating_price", nullable = false)
    private int ratingPrice;

    @Column(name = "rating_design", nullable = false)
    private int ratingDesign;

    @Column(name = "rating_features", nullable = false)
    private int ratingFeatures;

    @Column(name = "rating_usability", nullable = false)
    private int ratingUsability;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (id == null) id = UUID.randomUUID();
        createdAt = LocalDateTime.now();
    }
}
