package com.techmarket.authservice.application.port.in;

import com.techmarket.authservice.domain.model.User;

import java.util.UUID;

public interface GetUserUseCase {

    User getById(UUID id);

    User getByPhone(String phone);
}
