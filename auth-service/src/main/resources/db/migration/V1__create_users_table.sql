CREATE SCHEMA IF NOT EXISTS auth_service;

CREATE TABLE auth_service.users (
    id              UUID            PRIMARY KEY,
    phone           VARCHAR(20)     NOT NULL,
    name            VARCHAR(255),
    telegram_chat_id BIGINT,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX idx_users_phone ON auth_service.users (phone);
