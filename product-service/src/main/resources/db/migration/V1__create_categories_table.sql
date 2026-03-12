CREATE SCHEMA IF NOT EXISTS product_service;

CREATE TABLE product_service.categories (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name            VARCHAR(255) NOT NULL UNIQUE,
    slug            VARCHAR(255) NOT NULL UNIQUE,
    description     TEXT,
    parent_category_id UUID REFERENCES product_service.categories(id),
    image_url       VARCHAR(512)
);

CREATE INDEX idx_categories_slug ON product_service.categories(slug);
CREATE INDEX idx_categories_parent ON product_service.categories(parent_category_id);
