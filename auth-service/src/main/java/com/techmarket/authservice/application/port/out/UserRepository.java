package com.techmarket.authservice.application.port.out;

import com.techmarket.authservice.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findById(UUID id);

    Optional<User> findByPhone(String phone);

    Optional<User> findByEmail(String email);

    User save(User user);
}
