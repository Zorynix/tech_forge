CREATE SCHEMA IF NOT EXISTS order_service;

CREATE TABLE order_service.orders
(
    id          UUID            PRIMARY KEY,
    user_id     UUID            NOT NULL,
    status      VARCHAR(50)     NOT NULL DEFAULT 'CREATED',
    total_price NUMERIC(12, 2)  NOT NULL,
    phone       VARCHAR(20),
    created_at  TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_orders_user_id ON order_service.orders (user_id);
CREATE INDEX idx_orders_status ON order_service.orders (status);
CREATE INDEX idx_orders_created_at ON order_service.orders (created_at DESC);

CREATE TABLE order_service.order_items
(
    id           UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id     UUID            NOT NULL REFERENCES order_service.orders (id) ON DELETE CASCADE,
    product_id   UUID            NOT NULL,
    product_name VARCHAR(255)    NOT NULL,
    price        NUMERIC(12, 2)  NOT NULL,
    quantity     INT             NOT NULL CHECK (quantity > 0),
    image_url    VARCHAR(512)
);

CREATE INDEX idx_order_items_order_id ON order_service.order_items (order_id);
