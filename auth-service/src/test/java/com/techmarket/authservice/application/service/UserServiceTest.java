package com.techmarket.authservice.application.service;

import com.techmarket.authservice.application.port.out.UserRepository;
import com.techmarket.authservice.domain.exception.UserNotFoundException;
import com.techmarket.authservice.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User createUser(UUID id, String phone) {
        return new User(id, phone, null, "Test User", null, Instant.now(), Instant.now());
    }

    @Test
    @DisplayName("getById should return user when found")
    void getById_found_returnsUser() {
        UUID id = UUID.randomUUID();
        User user = createUser(id, "+79001234567");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.getById(id);

        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("getById should throw when user not found")
    void getById_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(id))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("getByPhone should return user when found")
    void getByPhone_found_returnsUser() {
        String phone = "+79001234567";
        User user = createUser(UUID.randomUUID(), phone);
        when(userRepository.findByPhone(phone)).thenReturn(Optional.of(user));

        User result = userService.getByPhone(phone);

        assertThat(result.getPhone()).isEqualTo(phone);
    }

    @Test
    @DisplayName("getByPhone should throw when user not found")
    void getByPhone_notFound_throws() {
        when(userRepository.findByPhone("+70000000000")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getByPhone("+70000000000"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("linkTelegram should update user telegramChatId")
    void linkTelegram_updatesUser() {
        UUID userId = UUID.randomUUID();
        User user = createUser(userId, "+79001234567");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.linkTelegram(userId, 123456789L);

        assertThat(user.getTelegramChatId()).isEqualTo(123456789L);
        verify(userRepository).save(user);
    }
}
