package com.techmarket.authservice.application.port.in;

import com.techmarket.authservice.domain.model.TokenPair;

public interface VerifyCodeUseCase {

    TokenPair verify(String identifier, String code);
}
