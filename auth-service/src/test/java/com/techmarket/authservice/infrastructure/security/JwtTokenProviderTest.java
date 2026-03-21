package com.techmarket.authservice.infrastructure.security;

import com.techmarket.authservice.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;
    private static final String SECRET = "test-secret-key-must-be-at-least-32-characters-long-for-hmac-sha256";

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider(SECRET, 3600000L, 2592000000L);
    }

    private User createUser() {
        return new User(UUID.randomUUID(), "+79001234567", "test@example.com",
                "Test User", null, Instant.now(), Instant.now());
    }

    @Test
    @DisplayName("should generate valid access token")
    void generateAccessToken_createsValidToken() {
        User user = createUser();

        String token = tokenProvider.generateAccessToken(user);

        assertThat(token).isNotBlank();
        assertThat(tokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("should generate valid refresh token")
    void generateRefreshToken_createsValidToken() {
        User user = createUser();

        String token = tokenProvider.generateRefreshToken(user);

        assertThat(token).isNotBlank();
        assertThat(tokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("should extract userId from token")
    void extractUserId_returnsCorrectId() {
        User user = createUser();
        String token = tokenProvider.generateAccessToken(user);

        UUID extractedId = tokenProvider.extractUserId(token);

        assertThat(extractedId).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("should extract phone from token")
    void extractPhone_returnsCorrectPhone() {
        User user = createUser();
        String token = tokenProvider.generateAccessToken(user);

        String phone = tokenProvider.extractPhone(token);

        assertThat(phone).isEqualTo(user.getPhone());
    }

    @Test
    @DisplayName("should return false for invalid token")
    void validateToken_invalidToken_returnsFalse() {
        assertThat(tokenProvider.validateToken("invalid.token.here")).isFalse();
    }

    @Test
    @DisplayName("should return false for expired token")
    void validateToken_expiredToken_returnsFalse() {
        JwtTokenProvider shortLivedProvider = new JwtTokenProvider(SECRET, -1000L, -1000L);
        User user = createUser();
        String token = shortLivedProvider.generateAccessToken(user);

        assertThat(tokenProvider.validateToken(token)).isFalse();
    }

    @Test
    @DisplayName("should return false for token signed with different secret")
    void validateToken_wrongSecret_returnsFalse() {
        JwtTokenProvider otherProvider = new JwtTokenProvider(
                "another-secret-key-that-is-at-least-32-characters-long!!", 3600000L, 2592000000L);
        User user = createUser();
        String token = otherProvider.generateAccessToken(user);

        assertThat(tokenProvider.validateToken(token)).isFalse();
    }
}
