CREATE TABLE notification_service.telegram_subscriptions (
    id UUID PRIMARY KEY,
    user_id UUID,
    chat_id BIGINT NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_telegram_subscriptions_user_id ON notification_service.telegram_subscriptions(user_id);
CREATE INDEX idx_telegram_subscriptions_phone ON notification_service.telegram_subscriptions(phone);
CREATE INDEX idx_telegram_subscriptions_chat_id ON notification_service.telegram_subscriptions(chat_id);
