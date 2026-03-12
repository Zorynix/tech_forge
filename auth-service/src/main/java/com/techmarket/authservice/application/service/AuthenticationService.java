package com.techmarket.authservice.application.service;

import com.techmarket.authservice.application.port.in.RefreshTokenUseCase;
import com.techmarket.authservice.application.port.in.RequestVerificationUseCase;
import com.techmarket.authservice.application.port.in.VerifyCodeUseCase;
import com.techmarket.authservice.application.port.out.EmailSender;
import com.techmarket.authservice.application.port.out.TokenProvider;
import com.techmarket.authservice.application.port.out.UserRepository;
import com.techmarket.authservice.application.port.out.VerificationCodeStore;
import com.techmarket.authservice.domain.exception.InvalidVerificationCodeException;
import com.techmarket.authservice.domain.exception.TokenExpiredException;
import com.techmarket.authservice.domain.exception.UserNotFoundException;
import com.techmarket.authservice.domain.model.TokenPair;
import com.techmarket.authservice.domain.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.UUID;

@Service
public class AuthenticationService implements RequestVerificationUseCase,
        VerifyCodeUseCase, RefreshTokenUseCase {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private static final Duration CODE_TTL = Duration.ofMinutes(5);
    private static final int CODE_LENGTH = 6;

    private final VerificationCodeStore codeStore;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final EmailSender emailSender;
    private final SecureRandom secureRandom;

    @Value("${app.jwt.access-token-expiration:3600000}")
    private long accessTokenExpiration;

    public AuthenticationService(VerificationCodeStore codeStore,
                                 UserRepository userRepository,
                                 TokenProvider tokenProvider,
                                 EmailSender emailSender) {
        this.codeStore = codeStore;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.emailSender = emailSender;
        this.secureRandom = new SecureRandom();
    }

    @Override
    public void requestCode(String identifier) {
        String code = generateCode();
        codeStore.save(identifier, code, CODE_TTL);

        if (isEmail(identifier)) {
            emailSender.sendVerificationCode(identifier, code);
        } else {
            // SMS sending – integrate with an SMS gateway in production
            log.info("Verification code for phone {}: {}", identifier, code);
        }
    }

    @Override
    @Transactional
    public TokenPair verify(String identifier, String code) {
        String storedCode = codeStore.find(identifier)
                .orElseThrow(() -> new InvalidVerificationCodeException(
                        "No verification code found. The code may have expired."));

        if (!storedCode.equals(code)) {
            throw new InvalidVerificationCodeException("Invalid verification code.");
        }

        codeStore.delete(identifier);

        User user;
        if (isEmail(identifier)) {
            user = userRepository.findByEmail(identifier)
                    .orElseGet(() -> {
                        User newUser = User.createNewWithEmail(identifier);
                        return userRepository.save(newUser);
                    });
        } else {
            user = userRepository.findByPhone(identifier)
                    .orElseGet(() -> {
                        User newUser = User.createNew(identifier);
                        return userRepository.save(newUser);
                    });
        }

        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshToken = tokenProvider.generateRefreshToken(user);

        return new TokenPair(accessToken, refreshToken, accessTokenExpiration / 1000);
    }

    @Override
    public TokenPair refresh(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new TokenExpiredException("Refresh token is invalid or expired.");
        }

        UUID userId = tokenProvider.extractUserId(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found for id: " + userId));

        String newAccessToken = tokenProvider.generateAccessToken(user);
        String newRefreshToken = tokenProvider.generateRefreshToken(user);

        return new TokenPair(newAccessToken, newRefreshToken, accessTokenExpiration / 1000);
    }

    private String generateCode() {
        int bound = (int) Math.pow(10, CODE_LENGTH);
        int code = secureRandom.nextInt(bound);
        return String.format("%0" + CODE_LENGTH + "d", code);
    }

    private boolean isEmail(String identifier) {
        return identifier != null && identifier.contains("@");
    }
}
