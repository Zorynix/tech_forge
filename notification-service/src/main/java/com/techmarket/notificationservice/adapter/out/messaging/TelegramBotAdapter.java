package com.techmarket.notificationservice.adapter.out.messaging;

import com.techmarket.notificationservice.application.port.out.TelegramSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
public class TelegramBotAdapter implements TelegramSender {

    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot{token}/sendMessage";

    private final RestTemplate restTemplate;
    private final String botToken;

    public TelegramBotAdapter(
            RestTemplate restTemplate,
            @Value("${app.telegram.bot-token}") String botToken) {
        this.restTemplate = restTemplate;
        this.botToken = botToken;
    }

    @Override
    public boolean sendMessage(Long chatId, String message) {
        try {
            String url = TELEGRAM_API_URL.replace("{token}", botToken);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = Map.of(
                    "chat_id", chatId,
                    "text", message,
                    "parse_mode", "HTML"
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(url, request, String.class);

            log.info("Telegram message sent to chatId={}", chatId);
            return true;
        } catch (Exception e) {
            log.error("Failed to send Telegram message to chatId={}: {}", chatId, e.getMessage());
            return false;
        }
    }
}
