package com.techmarket.authservice.application.service;

import com.techmarket.authservice.application.port.in.GetUserUseCase;
import com.techmarket.authservice.application.port.in.LinkTelegramUseCase;
import com.techmarket.authservice.application.port.out.UserRepository;
import com.techmarket.authservice.domain.exception.UserNotFoundException;
import com.techmarket.authservice.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService implements GetUserUseCase, LinkTelegramUseCase {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found for id: " + id));
    }

    @Override
    public User getByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found for phone: " + phone));
    }

    @Override
    @Transactional
    public void linkTelegram(UUID userId, Long chatId) {
        User user = getById(userId);
        user.setTelegramChatId(chatId);
        userRepository.save(user);
    }
}
