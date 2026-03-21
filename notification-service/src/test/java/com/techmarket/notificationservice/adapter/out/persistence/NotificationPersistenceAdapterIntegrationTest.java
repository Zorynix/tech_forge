package com.techmarket.notificationservice.adapter.out.persistence;

import com.techmarket.notificationservice.domain.model.Notification;
import com.techmarket.notificationservice.domain.model.NotificationChannel;
import com.techmarket.notificationservice.domain.model.NotificationStatus;
import com.techmarket.notificationservice.testconfig.BaseIntegrationTest;
import com.techmarket.notificationservice.testconfig.TestBeanOverrides;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestBeanOverrides.class)
class NotificationPersistenceAdapterIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private NotificationPersistenceAdapter adapter;

    @Test
    @DisplayName("should save and find notifications by userId")
    void saveAndFindByUserId() {
        UUID userId = UUID.randomUUID();
        Notification notification = Notification.createPending(
                userId, UUID.randomUUID(), NotificationChannel.TELEGRAM, "Test message");
        notification.markSent();

        adapter.save(notification);

        Page<Notification> page = adapter.findByUserId(userId, PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().getFirst().getStatus()).isEqualTo(NotificationStatus.SENT);
    }

    @Test
    @DisplayName("should save email notification")
    void saveEmailNotification() {
        UUID userId = UUID.randomUUID();
        Notification notification = Notification.createPending(
                userId, UUID.randomUUID(), NotificationChannel.EMAIL, "Order confirmed");
        notification.markFailed();

        adapter.save(notification);

        Page<Notification> page = adapter.findByUserId(userId, PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().getFirst().getChannel()).isEqualTo(NotificationChannel.EMAIL);
        assertThat(page.getContent().getFirst().getStatus()).isEqualTo(NotificationStatus.FAILED);
    }
}
