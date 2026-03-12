CREATE SCHEMA IF NOT EXISTS notification_service;

CREATE TABLE notification_service.notifications (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    order_id UUID NOT NULL,
    channel VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    sent_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_notifications_user_id ON notification_service.notifications(user_id);
CREATE INDEX idx_notifications_order_id ON notification_service.notifications(order_id);
CREATE INDEX idx_notifications_status ON notification_service.notifications(status);
CREATE INDEX idx_notifications_created_at ON notification_service.notifications(created_at DESC);
