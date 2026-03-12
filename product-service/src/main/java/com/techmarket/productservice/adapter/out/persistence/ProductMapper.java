package com.techmarket.productservice.adapter.out.persistence;

import com.techmarket.productservice.domain.model.Category;
import com.techmarket.productservice.domain.model.Product;
import com.techmarket.productservice.domain.model.Review;
import com.techmarket.productservice.domain.model.VideoReview;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toDomain(ProductJpaEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .categoryId(entity.getCategoryId())
                .imageUrls(entity.getImageUrls())
                .spinImages(entity.getSpinImages())
                .modelUrl(entity.getModelUrl())
                .specifications(entity.getSpecifications())
                .stockQuantity(entity.getStockQuantity())
                .active(entity.isActive())
                .averageRating(entity.getAverageRating())
                .reviewCount(entity.getReviewCount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public ProductJpaEntity toJpaEntity(Product product) {
        return ProductJpaEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .categoryId(product.getCategoryId())
                .imageUrls(product.getImageUrls())
                .spinImages(product.getSpinImages())
                .modelUrl(product.getModelUrl())
                .specifications(product.getSpecifications())
                .stockQuantity(product.getStockQuantity())
                .active(product.isActive())
                .averageRating(product.getAverageRating())
                .reviewCount(product.getReviewCount())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public Category toDomain(CategoryJpaEntity entity) {
        return Category.builder()
                .id(entity.getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .description(entity.getDescription())
                .parentCategoryId(entity.getParentCategoryId())
                .imageUrl(entity.getImageUrl())
                .build();
    }

    public CategoryJpaEntity toJpaEntity(Category category) {
        return CategoryJpaEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .parentCategoryId(category.getParentCategoryId())
                .imageUrl(category.getImageUrl())
                .build();
    }

    public VideoReview toDomain(VideoReviewJpaEntity entity) {
        return VideoReview.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .title(entity.getTitle())
                .videoUrl(entity.getVideoUrl())
                .thumbnailUrl(entity.getThumbnailUrl())
                .durationSeconds(entity.getDurationSeconds())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public VideoReviewJpaEntity toJpaEntity(VideoReview review) {
        return VideoReviewJpaEntity.builder()
                .id(review.getId())
                .productId(review.getProductId())
                .title(review.getTitle())
                .videoUrl(review.getVideoUrl())
                .thumbnailUrl(review.getThumbnailUrl())
                .durationSeconds(review.getDurationSeconds())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public Review toDomain(ReviewJpaEntity entity) {
        return Review.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .authorName(entity.getAuthorName())
                .authorId(entity.getAuthorId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .ratingQuality(entity.getRatingQuality())
                .ratingPrice(entity.getRatingPrice())
                .ratingDesign(entity.getRatingDesign())
                .ratingFeatures(entity.getRatingFeatures())
                .ratingUsability(entity.getRatingUsability())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public ReviewJpaEntity toJpaEntity(Review review) {
        return ReviewJpaEntity.builder()
                .id(review.getId())
                .productId(review.getProductId())
                .authorName(review.getAuthorName())
                .authorId(review.getAuthorId())
                .title(review.getTitle())
                .content(review.getContent())
                .ratingQuality(review.getRatingQuality())
                .ratingPrice(review.getRatingPrice())
                .ratingDesign(review.getRatingDesign())
                .ratingFeatures(review.getRatingFeatures())
                .ratingUsability(review.getRatingUsability())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
