package com.techmarket.authservice.adapter.in.web;

import com.techmarket.authservice.adapter.in.web.dto.RefreshTokenRequest;
import com.techmarket.authservice.adapter.in.web.dto.RequestCodeRequest;
import com.techmarket.authservice.adapter.in.web.dto.TokenResponse;
import com.techmarket.authservice.adapter.in.web.dto.VerifyCodeRequest;
import com.techmarket.authservice.application.port.in.RefreshTokenUseCase;
import com.techmarket.authservice.application.port.in.RequestVerificationUseCase;
import com.techmarket.authservice.application.port.in.VerifyCodeUseCase;
import com.techmarket.authservice.domain.model.TokenPair;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final RequestVerificationUseCase requestVerificationUseCase;
    private final VerifyCodeUseCase verifyCodeUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public AuthController(RequestVerificationUseCase requestVerificationUseCase,
                          VerifyCodeUseCase verifyCodeUseCase,
                          RefreshTokenUseCase refreshTokenUseCase) {
        this.requestVerificationUseCase = requestVerificationUseCase;
        this.verifyCodeUseCase = verifyCodeUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @PostMapping("/request-code")
    public ResponseEntity<Void> requestCode(@Valid @RequestBody RequestCodeRequest request) {
        String identifier = (request.phone() != null && !request.phone().isBlank())
                ? request.phone() : request.email();
        requestVerificationUseCase.requestCode(identifier);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<TokenResponse> verify(@Valid @RequestBody VerifyCodeRequest request) {
        TokenPair tokenPair = verifyCodeUseCase.verify(request.identifier(), request.code());
        return ResponseEntity.ok(new TokenResponse(
                tokenPair.accessToken(),
                tokenPair.refreshToken(),
                tokenPair.expiresIn()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        TokenPair tokenPair = refreshTokenUseCase.refresh(request.refreshToken());
        return ResponseEntity.ok(new TokenResponse(
                tokenPair.accessToken(),
                tokenPair.refreshToken(),
                tokenPair.expiresIn()
        ));
    }
}
