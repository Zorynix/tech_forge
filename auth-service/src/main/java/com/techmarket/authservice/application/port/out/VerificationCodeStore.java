package com.techmarket.authservice.application.port.out;

import java.time.Duration;
import java.util.Optional;

public interface VerificationCodeStore {

    void save(String phone, String code, Duration ttl);

    Optional<String> find(String phone);

    void delete(String phone);
}
