CREATE TABLE product_service.video_reviews (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id       UUID NOT NULL REFERENCES product_service.products(id) ON DELETE CASCADE,
    title            VARCHAR(255) NOT NULL,
    video_url        VARCHAR(512) NOT NULL,
    thumbnail_url    VARCHAR(512),
    duration_seconds INTEGER DEFAULT 0,
    created_at       TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_video_reviews_product ON product_service.video_reviews(product_id);
