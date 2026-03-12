CREATE TABLE product_service.products (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    price           NUMERIC(12, 2) NOT NULL,
    category_id     UUID NOT NULL REFERENCES product_service.categories(id),
    image_urls      JSONB DEFAULT '[]'::jsonb,
    specifications  JSONB DEFAULT '{}'::jsonb,
    stock_quantity  INTEGER NOT NULL DEFAULT 0,
    active          BOOLEAN NOT NULL DEFAULT true,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_products_category ON product_service.products(category_id);
CREATE INDEX idx_products_name ON product_service.products USING gin(to_tsvector('english', name));
CREATE INDEX idx_products_active ON product_service.products(active);
CREATE INDEX idx_products_price ON product_service.products(price);
