package com.techmarket.authservice.adapter.out.persistence;

import com.techmarket.authservice.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getName(),
                entity.getTelegramChatId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public UserJpaEntity toEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setPhone(user.getPhone());
        entity.setEmail(user.getEmail());
        entity.setName(user.getName());
        entity.setTelegramChatId(user.getTelegramChatId());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        return entity;
    }
}
