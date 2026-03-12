package com.techmarket.notificationservice.adapter.out.messaging;

import com.techmarket.notificationservice.application.port.out.UserServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class UserServiceClientAdapter implements UserServiceClient {

    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public UserServiceClientAdapter(
            RestTemplate restTemplate,
            @Value("${app.auth-service.url}") String authServiceUrl) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
    }

    @Override
    public void updateTelegramChatId(UUID userId, Long chatId) {
        try {
            String url = authServiceUrl + "/internal/users/" + userId + "/telegram";
            restTemplate.put(url, Map.of("chatId", chatId));
            log.info("Updated telegramChatId for userId={} in auth-service", userId);
        } catch (Exception e) {
            log.warn("Failed to update telegramChatId for userId={}: {}", userId, e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserInfo getUserById(UUID userId) {
        try {
            String url = authServiceUrl + "/internal/users/" + userId;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null) {
                return null;
            }

            return new UserInfo(
                    UUID.fromString((String) response.get("id")),
                    (String) response.get("phone"),
                    (String) response.get("email"),
                    (String) response.get("name"),
                    response.get("telegramChatId") != null
                            ? ((Number) response.get("telegramChatId")).longValue()
                            : null
            );
        } catch (Exception e) {
            log.warn("Failed to fetch user info for userId={}: {}", userId, e.getMessage());
            return null;
        }
    }
}
