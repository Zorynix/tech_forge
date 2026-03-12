package com.techmarket.notificationservice.application.service;

import com.techmarket.notificationservice.application.port.in.GetNotificationHistoryUseCase;
import com.techmarket.notificationservice.application.port.in.ProcessOrderEventUseCase;
import com.techmarket.notificationservice.application.port.out.EmailSender;
import com.techmarket.notificationservice.application.port.out.NotificationRepository;
import com.techmarket.notificationservice.application.port.out.TelegramSender;
import com.techmarket.notificationservice.application.port.out.TelegramSubscriptionRepository;
import com.techmarket.notificationservice.application.port.out.UserServiceClient;
import com.techmarket.notificationservice.domain.event.OrderEvent;
import com.techmarket.notificationservice.domain.model.Notification;
import com.techmarket.notificationservice.domain.model.NotificationChannel;
import com.techmarket.notificationservice.domain.model.TelegramSubscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService implements ProcessOrderEventUseCase, GetNotificationHistoryUseCase {

    private final TelegramSubscriptionRepository subscriptionRepository;
    private final NotificationRepository notificationRepository;
    private final TelegramSender telegramSender;
    private final EmailSender emailSender;
    private final UserServiceClient userServiceClient;

    @Override
    public void process(OrderEvent event) {
        log.info("Processing order event: orderId={}, status={}", event.orderId(), event.status());

        String message = formatMessage(event);

        // Send Telegram notification if subscription exists
        sendTelegramNotification(event, message);

        // Send email notification
        sendEmailNotification(event, message);
    }

    private void sendTelegramNotification(OrderEvent event, String message) {
        Optional<TelegramSubscription> subscription = findSubscription(event);

        if (subscription.isEmpty()) {
            log.info("No Telegram subscription for userId={}, skipping TG notification", event.userId());
            return;
        }

        TelegramSubscription sub = subscription.get();
        Notification notification = Notification.createPending(
                event.userId(),
                event.orderId(),
                NotificationChannel.TELEGRAM,
                message
        );

        boolean sent = telegramSender.sendMessage(sub.getChatId(), message);

        if (sent) {
            notification.markSent();
            log.info("Telegram notification sent for orderId={}", event.orderId());
        } else {
            notification.markFailed();
            log.error("Failed to send Telegram notification for orderId={}", event.orderId());
        }

        notificationRepository.save(notification);
    }

    private void sendEmailNotification(OrderEvent event, String message) {
        if (event.userId() == null) return;

        UserServiceClient.UserInfo userInfo = null;
        try {
            userInfo = userServiceClient.getUserById(event.userId());
        } catch (Exception e) {
            log.warn("Failed to fetch user for email notification: {}", e.getMessage());
        }

        if (userInfo == null || userInfo.email() == null || userInfo.email().isBlank()) {
            log.info("No email for userId={}, skipping email notification", event.userId());
            return;
        }

        String shortId = event.orderId().toString().substring(0, 8);
        String subject = "⛩️ Tech Forge — Заказ #" + shortId;
        String htmlBody = buildOrderEmailHtml(event, shortId, userInfo.name());

        Notification notification = Notification.createPending(
                event.userId(),
                event.orderId(),
                NotificationChannel.EMAIL,
                message
        );

        boolean sent = emailSender.sendOrderNotification(userInfo.email(), subject, htmlBody);

        if (sent) {
            notification.markSent();
            log.info("Email notification sent for orderId={} to {}", event.orderId(), userInfo.email());
        } else {
            notification.markFailed();
            log.error("Failed to send email notification for orderId={}", event.orderId());
        }

        notificationRepository.save(notification);
    }

    private String buildOrderEmailHtml(OrderEvent event, String shortId, String userName) {
        String statusEmoji = switch (event.status()) {
            case "CREATED" -> "🛒";
            case "CONFIRMED" -> "✅";
            case "PROCESSING" -> "📦";
            case "READY_FOR_PICKUP" -> "🎉";
            case "COMPLETED" -> "✨";
            case "CANCELLED" -> "❌";
            default -> "ℹ️";
        };

        String statusText = switch (event.status()) {
            case "CREATED" -> "Создан";
            case "CONFIRMED" -> "Подтверждён";
            case "PROCESSING" -> "Собирается";
            case "READY_FOR_PICKUP" -> "Готов к выдаче";
            case "COMPLETED" -> "Выдан";
            case "CANCELLED" -> "Отменён";
            default -> event.status();
        };

        String greeting = userName != null && !userName.isBlank()
                ? userName : "клиент";

        return "<!DOCTYPE html>" +
            "<html lang=\"ru\">" +
            "<head><meta charset=\"UTF-8\"></head>" +
            "<body style=\"margin:0;padding:0;background:#0f0f1a;font-family:'Segoe UI',Arial,sans-serif;\">" +
            "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#0f0f1a;padding:40px 0;\">" +
            "<tr><td align=\"center\">" +
            "<table width=\"520\" cellpadding=\"0\" cellspacing=\"0\" style=\"" +
                "background:linear-gradient(180deg,#1a1a2e 0%,#16213e 100%);" +
                "border-radius:20px;" +
                "border:1px solid rgba(233,69,96,0.3);" +
                "box-shadow:0 20px 60px rgba(0,0,0,0.5),0 0 40px rgba(233,69,96,0.1);" +
                "overflow:hidden;\">" +
            "<tr><td style=\"padding:0;\">" +
                "<div style=\"background:linear-gradient(135deg,#e94560 0%,#c23152 50%,#e94560 100%);" +
                    "padding:30px 40px;text-align:center;\">" +
                    "<div style=\"font-size:24px;letter-spacing:12px;margin-bottom:8px;\">⛩️ 🌸 ⛩️</div>" +
                    "<h1 style=\"margin:0;font-size:26px;font-weight:700;color:#ffffff;letter-spacing:3px;" +
                        "text-shadow:0 2px 10px rgba(0,0,0,0.3);\">Tech Forge</h1>" +
                    "<div style=\"margin-top:6px;font-size:11px;color:rgba(255,255,255,0.8);" +
                        "letter-spacing:6px;\">テックフォージ</div>" +
                "</div>" +
            "</td></tr>" +
            "<tr><td style=\"padding:40px 40px 20px;\">" +
                "<p style=\"margin:0 0 20px;font-size:18px;color:#e0e0e0;line-height:1.5;\">" +
                    "Здравствуйте, " + greeting + "!" +
                "</p>" +
                "<div style=\"text-align:center;margin:30px 0;\">" +
                    "<div style=\"display:inline-block;padding:20px 40px;" +
                        "background:linear-gradient(135deg,#1a1a2e 0%,#16213e 100%);" +
                        "border:2px solid #e94560;border-radius:16px;" +
                        "box-shadow:0 4px 15px rgba(233,69,96,0.3);\">" +
                        "<div style=\"font-size:32px;margin-bottom:8px;\">" + statusEmoji + "</div>" +
                        "<div style=\"font-size:13px;color:#e94560;letter-spacing:3px;margin-bottom:4px;\">ЗАКАЗ #" + shortId.toUpperCase() + "</div>" +
                        "<div style=\"font-size:20px;font-weight:700;color:#ffffff;\">" + statusText + "</div>" +
                    "</div>" +
                "</div>" +
                "<div style=\"text-align:center;margin:25px 0;color:rgba(233,69,96,0.4);font-size:14px;letter-spacing:8px;\">" +
                    "✦ ✦ ✦" +
                "</div>" +
                "<p style=\"margin:0;font-size:13px;color:#666;text-align:center;line-height:1.6;\">" +
                    "Вы получите уведомление при изменении статуса заказа." +
                "</p>" +
            "</td></tr>" +
            "<tr><td style=\"padding:20px 40px 30px;\">" +
                "<div style=\"border-top:1px solid rgba(233,69,96,0.15);padding-top:20px;text-align:center;\">" +
                    "<div style=\"font-size:18px;letter-spacing:6px;margin-bottom:10px;\">🌸🏯🌸</div>" +
                    "<p style=\"margin:0 0 4px;font-size:12px;color:#555;\">© Tech Forge 2026</p>" +
                    "<p style=\"margin:0;font-size:11px;color:#444;letter-spacing:2px;\">日本のテクノロジー ・ 品質と信頼</p>" +
                "</div>" +
            "</td></tr>" +
            "</table>" +
            "</td></tr>" +
            "</table>" +
            "</body></html>";
    }

    @Override
    public Page<Notification> getByUserId(UUID userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable);
    }

    private Optional<TelegramSubscription> findSubscription(OrderEvent event) {
        // First try to find by phone from the event
        if (event.phone() != null) {
            Optional<TelegramSubscription> byPhone = subscriptionRepository.findByPhone(event.phone());
            if (byPhone.isPresent()) {
                return byPhone;
            }
        }

        // Then try to find by userId
        if (event.userId() != null) {
            Optional<TelegramSubscription> byUser = subscriptionRepository.findByUserId(event.userId());
            if (byUser.isPresent()) {
                return byUser;
            }

            // As a last resort, try fetching user info from auth-service to get phone
            try {
                UserServiceClient.UserInfo userInfo = userServiceClient.getUserById(event.userId());
                if (userInfo != null && userInfo.phone() != null) {
                    return subscriptionRepository.findByPhone(userInfo.phone());
                }
            } catch (Exception e) {
                log.warn("Failed to fetch user info for userId={}: {}", event.userId(), e.getMessage());
            }
        }

        return Optional.empty();
    }

    private String formatMessage(OrderEvent event) {
        String shortId = event.orderId().toString().substring(0, 8);

        return switch (event.status()) {
            case "CREATED" -> "\uD83D\uDED2 \u0412\u0430\u0448 \u0437\u0430\u043A\u0430\u0437 #" + shortId + " \u0441\u043E\u0437\u0434\u0430\u043D! \u041E\u0436\u0438\u0434\u0430\u0439\u0442\u0435 \u043F\u043E\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043D\u0438\u044F.";
            case "CONFIRMED" -> "\u2705 \u0417\u0430\u043A\u0430\u0437 #" + shortId + " \u043F\u043E\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0451\u043D! \u041C\u044B \u043D\u0430\u0447\u0438\u043D\u0430\u0435\u043C \u0441\u0431\u043E\u0440\u043A\u0443.";
            case "PROCESSING" -> "\uD83D\uDCE6 \u0417\u0430\u043A\u0430\u0437 #" + shortId + " \u0441\u043E\u0431\u0438\u0440\u0430\u0435\u0442\u0441\u044F \u043D\u0430 \u0441\u043A\u043B\u0430\u0434\u0435.";
            case "READY_FOR_PICKUP" -> "\uD83C\uDF89 \u0417\u0430\u043A\u0430\u0437 #" + shortId + " \u0433\u043E\u0442\u043E\u0432 \u043A \u0432\u044B\u0434\u0430\u0447\u0435! \u041F\u0440\u0438\u0445\u043E\u0434\u0438\u0442\u0435 \u0432 \u041F\u0412\u0417.";
            case "COMPLETED" -> "\u2728 \u0417\u0430\u043A\u0430\u0437 #" + shortId + " \u0432\u044B\u0434\u0430\u043D. \u0421\u043F\u0430\u0441\u0438\u0431\u043E \u0437\u0430 \u043F\u043E\u043A\u0443\u043F\u043A\u0443!";
            case "CANCELLED" -> "\u274C \u0417\u0430\u043A\u0430\u0437 #" + shortId + " \u043E\u0442\u043C\u0435\u043D\u0451\u043D.";
            default -> "\u2139\uFE0F \u0417\u0430\u043A\u0430\u0437 #" + shortId + ": " + event.status();
        };
    }
}
