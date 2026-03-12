package com.techmarket.authservice.application.port.in;

import com.techmarket.authservice.domain.model.TokenPair;

public interface RefreshTokenUseCase {

    TokenPair refresh(String refreshToken);
}
