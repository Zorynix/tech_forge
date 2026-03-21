package com.techmarket.notificationservice.testconfig;

import com.techmarket.notificationservice.application.port.out.EmailSender;
import com.techmarket.notificationservice.application.port.out.TelegramSender;
import com.techmarket.notificationservice.application.port.out.UserServiceClient;
import com.techmarket.notificationservice.infrastructure.config.TelegramPollingService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestBeanOverrides {

    @Bean
    public TelegramPollingService telegramPollingService() {
        return mock(TelegramPollingService.class);
    }

    @Bean
    public TelegramSender telegramSender() {
        return mock(TelegramSender.class);
    }

    @Bean
    public EmailSender emailSender() {
        return mock(EmailSender.class);
    }

    @Bean
    public UserServiceClient userServiceClient() {
        return mock(UserServiceClient.class);
    }
}
