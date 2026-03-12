package com.techmarket.notificationservice.application.service;

import com.techmarket.notificationservice.application.port.in.RegisterTelegramUseCase;
import com.techmarket.notificationservice.application.port.out.TelegramSubscriptionRepository;
import com.techmarket.notificationservice.domain.model.TelegramSubscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramSubscriptionService implements RegisterTelegramUseCase {

    private final TelegramSubscriptionRepository subscriptionRepository;

    @Override
    public void register(String phone, Long chatId) {
        log.info("Registering Telegram subscription: phone={}, chatId={}", phone, chatId);

        // Check if subscription already exists for this chat
        Optional<TelegramSubscription> existingByChatId = subscriptionRepository.findByChatId(chatId);
        if (existingByChatId.isPresent()) {
            TelegramSubscription existing = existingByChatId.get();
            existing.setPhone(phone);
            existing.setActive(true);
            subscriptionRepository.save(existing);
            log.info("Updated existing Telegram subscription for chatId={}", chatId);
            return;
        }

        // Check if subscription exists for this phone
        Optional<TelegramSubscription> existingByPhone = subscriptionRepository.findByPhone(phone);
        if (existingByPhone.isPresent()) {
            TelegramSubscription existing = existingByPhone.get();
            existing.setChatId(chatId);
            existing.setActive(true);
            subscriptionRepository.save(existing);
            log.info("Updated existing Telegram subscription for phone={}", phone);
            return;
        }

        // Create new subscription
        TelegramSubscription subscription = TelegramSubscription.createNew(phone, chatId);
        subscriptionRepository.save(subscription);
        log.info("Created new Telegram subscription: phone={}, chatId={}", phone, chatId);
    }
}
