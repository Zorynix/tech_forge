package com.techmarket.notificationservice.infrastructure.config;

import com.techmarket.notificationservice.application.port.in.ConfirmLinkCodeUseCase;
import com.techmarket.notificationservice.application.port.out.TelegramSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class TelegramPollingService implements CommandLineRunner {

    private static final String TELEGRAM_API = "https://api.telegram.org/bot";

    private final RestTemplate restTemplate;
    private final String botToken;
    private final ConfirmLinkCodeUseCase confirmLinkCodeUseCase;
    private final TelegramSender telegramSender;
    private final AtomicLong offset = new AtomicLong(0);

    public TelegramPollingService(
            RestTemplate restTemplate,
            @Value("${app.telegram.bot-token}") String botToken,
            ConfirmLinkCodeUseCase confirmLinkCodeUseCase,
            TelegramSender telegramSender) {
        this.restTemplate = restTemplate;
        this.botToken = botToken;
        this.confirmLinkCodeUseCase = confirmLinkCodeUseCase;
        this.telegramSender = telegramSender;
    }

    @Override
    public void run(String... args) {
        // Delete any existing webhook so polling works
        try {
            restTemplate.getForObject(
                    TELEGRAM_API + botToken + "/deleteWebhook",
                    String.class);
            log.info("Telegram webhook deleted, starting long polling");
        } catch (Exception e) {
            log.warn("Failed to delete Telegram webhook: {}", e.getMessage());
        }

        Thread pollingThread = new Thread(this::pollLoop, "telegram-polling");
        pollingThread.setDaemon(true);
        pollingThread.start();
    }

    @SuppressWarnings("unchecked")
    private void pollLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String url = TELEGRAM_API + botToken
                        + "/getUpdates?timeout=30&offset=" + offset.get();

                ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                        url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {});

                Map<String, Object> body = response.getBody();
                if (body == null || !Boolean.TRUE.equals(body.get("ok"))) {
                    Thread.sleep(5000);
                    continue;
                }

                List<Map<String, Object>> updates = (List<Map<String, Object>>) body.get("result");
                if (updates == null || updates.isEmpty()) {
                    continue;
                }

                for (Map<String, Object> update : updates) {
                    long updateId = ((Number) update.get("update_id")).longValue();
                    offset.set(updateId + 1);

                    try {
                        processUpdate(update);
                    } catch (Exception e) {
                        log.error("Error processing Telegram update {}", updateId, e);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Telegram polling error: {}", e.getMessage());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void processUpdate(Map<String, Object> update) {
        if (!update.containsKey("message")) {
            return;
        }

        Map<String, Object> message = (Map<String, Object>) update.get("message");
        Map<String, Object> chat = (Map<String, Object>) message.get("chat");
        Long chatId = ((Number) chat.get("id")).longValue();
        String text = (String) message.get("text");

        if (text == null) {
            return;
        }

        log.info("Telegram message from chatId={}: {}", chatId, text);

        if (text.startsWith("/start")) {
            handleStartCommand(chatId, text);
        } else if (text.equals("/unlink")) {
            telegramSender.sendMessage(chatId,
                    "Для отвязки Telegram перейдите в настройки профиля на сайте.");
        }
    }

    private void handleStartCommand(Long chatId, String text) {
        String[] parts = text.split("\\s+", 2);

        if (parts.length < 2 || parts[1].isBlank()) {
            telegramSender.sendMessage(chatId,
                    "Добро пожаловать в Tech Forge! 👋\n\n" +
                    "Для привязки аккаунта:\n" +
                    "1. Войдите на сайт Tech Forge\n" +
                    "2. Перейдите в профиль\n" +
                    "3. Нажмите «Привязать Telegram»\n" +
                    "4. Перейдите по сгенерированной ссылке\n\n" +
                    "После привязки вы будете получать уведомления о статусе заказов.");
            return;
        }

        String code = parts[1].trim();
        UUID userId = confirmLinkCodeUseCase.confirmCode(code, chatId);
        if (userId != null) {
            telegramSender.sendMessage(chatId,
                    "✅ Telegram успешно привязан к вашему аккаунту Tech Forge!\n\n" +
                    "Теперь вы будете получать уведомления о статусе ваших заказов.");
        } else {
            telegramSender.sendMessage(chatId,
                    "❌ Код недействителен или истёк.\n\n" +
                    "Перейдите в профиль на сайте и сгенерируйте новый код привязки.");
        }
    }
}
