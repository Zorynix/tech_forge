package com.techmarket.notificationservice.application.service;

import com.techmarket.notificationservice.application.port.out.*;
import com.techmarket.notificationservice.domain.event.OrderEvent;
import com.techmarket.notificationservice.domain.model.Notification;
import com.techmarket.notificationservice.domain.model.NotificationStatus;
import com.techmarket.notificationservice.domain.model.TelegramSubscription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private TelegramSubscriptionRepository subscriptionRepository;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private TelegramSender telegramSender;
    @Mock
    private EmailSender emailSender;
    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private NotificationService notificationService;

    private OrderEvent createEvent(String status) {
        return new OrderEvent(UUID.randomUUID(), UUID.randomUUID(), status,
                "+79001234567", "Test message", Instant.now());
    }

    @Nested
    @DisplayName("process")
    class Process {

        @Test
        @DisplayName("should send Telegram notification when subscription exists by phone")
        void process_withTelegramSubscription_sendsTelegram() {
            OrderEvent event = createEvent("CREATED");
            TelegramSubscription sub = TelegramSubscription.createNew("+79001234567", 123456L);
            when(subscriptionRepository.findByPhone(event.phone())).thenReturn(Optional.of(sub));
            when(telegramSender.sendMessage(eq(123456L), anyString())).thenReturn(true);
            when(userServiceClient.getUserById(event.userId())).thenReturn(null);

            notificationService.process(event);

            verify(telegramSender).sendMessage(eq(123456L), anyString());

            ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
            verify(notificationRepository, atLeastOnce()).save(captor.capture());

            boolean hasSentTelegram = captor.getAllValues().stream()
                    .anyMatch(n -> n.getStatus() == NotificationStatus.SENT);
            assertThat(hasSentTelegram).isTrue();
        }

        @Test
        @DisplayName("should save FAILED notification when Telegram send fails")
        void process_telegramFails_savesFailed() {
            OrderEvent event = createEvent("CONFIRMED");
            TelegramSubscription sub = TelegramSubscription.createNew("+79001234567", 123456L);
            when(subscriptionRepository.findByPhone(event.phone())).thenReturn(Optional.of(sub));
            when(telegramSender.sendMessage(eq(123456L), anyString())).thenReturn(false);
            when(userServiceClient.getUserById(event.userId())).thenReturn(null);

            notificationService.process(event);

            ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
            verify(notificationRepository, atLeastOnce()).save(captor.capture());

            boolean hasFailedTelegram = captor.getAllValues().stream()
                    .anyMatch(n -> n.getStatus() == NotificationStatus.FAILED);
            assertThat(hasFailedTelegram).isTrue();
        }

        @Test
        @DisplayName("should skip Telegram when no subscription exists")
        void process_noSubscription_skipsTelegram() {
            OrderEvent event = createEvent("CREATED");
            when(subscriptionRepository.findByPhone(event.phone())).thenReturn(Optional.empty());
            when(subscriptionRepository.findByUserId(event.userId())).thenReturn(Optional.empty());
            when(userServiceClient.getUserById(event.userId())).thenReturn(null);

            notificationService.process(event);

            verifyNoInteractions(telegramSender);
        }

        @Test
        @DisplayName("should send email when user has email")
        void process_withEmail_sendsEmail() {
            OrderEvent event = createEvent("CREATED");
            when(subscriptionRepository.findByPhone(event.phone())).thenReturn(Optional.empty());
            when(subscriptionRepository.findByUserId(event.userId())).thenReturn(Optional.empty());

            UserServiceClient.UserInfo userInfo = new UserServiceClient.UserInfo(
                    event.userId(), "+79001234567", "test@example.com", "Test User", null);
            when(userServiceClient.getUserById(event.userId())).thenReturn(userInfo);
            when(emailSender.sendOrderNotification(eq("test@example.com"), anyString(), anyString())).thenReturn(true);

            notificationService.process(event);

            verify(emailSender).sendOrderNotification(eq("test@example.com"), anyString(), anyString());
        }

        @Test
        @DisplayName("should skip email when user has no email")
        void process_noEmail_skipsEmail() {
            OrderEvent event = createEvent("CREATED");
            when(subscriptionRepository.findByPhone(event.phone())).thenReturn(Optional.empty());
            when(subscriptionRepository.findByUserId(event.userId())).thenReturn(Optional.empty());

            UserServiceClient.UserInfo userInfo = new UserServiceClient.UserInfo(
                    event.userId(), "+79001234567", null, "Test User", null);
            when(userServiceClient.getUserById(event.userId())).thenReturn(userInfo);

            notificationService.process(event);

            verifyNoInteractions(emailSender);
        }
    }

    @Nested
    @DisplayName("getByUserId")
    class GetByUserId {

        @Test
        @DisplayName("should return notification history")
        void getByUserId_returnsHistory() {
            UUID userId = UUID.randomUUID();
            Page<Notification> page = new PageImpl<>(List.of());
            when(notificationRepository.findByUserId(userId, PageRequest.of(0, 20))).thenReturn(page);

            Page<Notification> result = notificationService.getByUserId(userId, PageRequest.of(0, 20));

            assertThat(result).isNotNull();
        }
    }
}
