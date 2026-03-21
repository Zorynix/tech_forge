package com.techmarket.authservice.application.service;

import com.techmarket.authservice.application.port.out.EmailSender;
import com.techmarket.authservice.application.port.out.TokenProvider;
import com.techmarket.authservice.application.port.out.UserRepository;
import com.techmarket.authservice.application.port.out.VerificationCodeStore;
import com.techmarket.authservice.domain.exception.InvalidVerificationCodeException;
import com.techmarket.authservice.domain.exception.TokenExpiredException;
import com.techmarket.authservice.domain.exception.UserNotFoundException;
import com.techmarket.authservice.domain.model.TokenPair;
import com.techmarket.authservice.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private VerificationCodeStore codeStore;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private EmailSender emailSender;

    private AuthenticationService service;

    @BeforeEach
    void setUp() {
        service = new AuthenticationService(codeStore, userRepository, tokenProvider, emailSender);
    }

    @Nested
    @DisplayName("requestCode")
    class RequestCode {

        @Test
        @DisplayName("should save code and send email when identifier is email")
        void requestCode_withEmail_sendsEmail() {
            String email = "test@example.com";

            service.requestCode(email);

            ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
            verify(codeStore).save(eq(email), codeCaptor.capture(), eq(Duration.ofMinutes(5)));
            String code = codeCaptor.getValue();
            assertThat(code).hasSize(6).containsOnlyDigits();
            verify(emailSender).sendVerificationCode(email, code);
        }

        @Test
        @DisplayName("should save code and log when identifier is phone")
        void requestCode_withPhone_doesNotSendEmail() {
            String phone = "+79001234567";

            service.requestCode(phone);

            verify(codeStore).save(eq(phone), anyString(), eq(Duration.ofMinutes(5)));
            verifyNoInteractions(emailSender);
        }
    }

    @Nested
    @DisplayName("verify")
    class Verify {

        @Test
        @DisplayName("should create new user and return tokens for valid email code")
        void verify_newEmailUser_createsUserAndReturnsTokens() {
            String email = "new@example.com";
            String code = "123456";
            User newUser = new User(UUID.randomUUID(), null, email, null, null, Instant.now(), Instant.now());

            when(codeStore.find(email)).thenReturn(Optional.of(code));
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
            when(userRepository.save(any(User.class))).thenReturn(newUser);
            when(tokenProvider.generateAccessToken(newUser)).thenReturn("access-token");
            when(tokenProvider.generateRefreshToken(newUser)).thenReturn("refresh-token");

            TokenPair result = service.verify(email, code);

            assertThat(result.accessToken()).isEqualTo("access-token");
            assertThat(result.refreshToken()).isEqualTo("refresh-token");
            verify(codeStore).delete(email);
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("should return tokens for existing phone user")
        void verify_existingPhoneUser_returnsTokens() {
            String phone = "+79001234567";
            String code = "654321";
            User existingUser = new User(UUID.randomUUID(), phone, null, "John", null, Instant.now(), Instant.now());

            when(codeStore.find(phone)).thenReturn(Optional.of(code));
            when(userRepository.findByPhone(phone)).thenReturn(Optional.of(existingUser));
            when(tokenProvider.generateAccessToken(existingUser)).thenReturn("access-token");
            when(tokenProvider.generateRefreshToken(existingUser)).thenReturn("refresh-token");

            TokenPair result = service.verify(phone, code);

            assertThat(result.accessToken()).isEqualTo("access-token");
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw when code not found (expired)")
        void verify_expiredCode_throws() {
            when(codeStore.find("test@example.com")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.verify("test@example.com", "123456"))
                    .isInstanceOf(InvalidVerificationCodeException.class)
                    .hasMessageContaining("expired");
        }

        @Test
        @DisplayName("should throw when code is wrong")
        void verify_wrongCode_throws() {
            when(codeStore.find("test@example.com")).thenReturn(Optional.of("999999"));

            assertThatThrownBy(() -> service.verify("test@example.com", "123456"))
                    .isInstanceOf(InvalidVerificationCodeException.class)
                    .hasMessageContaining("Invalid");
        }
    }

    @Nested
    @DisplayName("refresh")
    class Refresh {

        @Test
        @DisplayName("should return new token pair for valid refresh token")
        void refresh_validToken_returnsNewTokenPair() {
            String refreshToken = "valid-refresh";
            UUID userId = UUID.randomUUID();
            User user = new User(userId, "+79001234567", null, "John", null, Instant.now(), Instant.now());

            when(tokenProvider.validateToken(refreshToken)).thenReturn(true);
            when(tokenProvider.extractUserId(refreshToken)).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(tokenProvider.generateAccessToken(user)).thenReturn("new-access");
            when(tokenProvider.generateRefreshToken(user)).thenReturn("new-refresh");

            TokenPair result = service.refresh(refreshToken);

            assertThat(result.accessToken()).isEqualTo("new-access");
            assertThat(result.refreshToken()).isEqualTo("new-refresh");
        }

        @Test
        @DisplayName("should throw for invalid refresh token")
        void refresh_invalidToken_throws() {
            when(tokenProvider.validateToken("bad-token")).thenReturn(false);

            assertThatThrownBy(() -> service.refresh("bad-token"))
                    .isInstanceOf(TokenExpiredException.class);
        }

        @Test
        @DisplayName("should throw when user not found")
        void refresh_userNotFound_throws() {
            UUID userId = UUID.randomUUID();
            when(tokenProvider.validateToken("token")).thenReturn(true);
            when(tokenProvider.extractUserId("token")).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.refresh("token"))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }
}
