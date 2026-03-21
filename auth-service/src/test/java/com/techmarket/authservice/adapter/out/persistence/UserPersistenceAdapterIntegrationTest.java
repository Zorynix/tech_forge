package com.techmarket.authservice.adapter.out.persistence;

import com.techmarket.authservice.domain.model.User;
import com.techmarket.authservice.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserPersistenceAdapterIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserPersistenceAdapter adapter;

    @Test
    @DisplayName("should save and find user by phone")
    void saveAndFindByPhone() {
        User user = User.createNew("+79001111111");
        user.setName("Test User");

        User saved = adapter.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPhone()).isEqualTo("+79001111111");

        Optional<User> found = adapter.findByPhone("+79001111111");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test User");
    }

    @Test
    @DisplayName("should save and find user by email")
    void saveAndFindByEmail() {
        User user = User.createNewWithEmail("persist-test@example.com");
        user.setName("Email User");

        adapter.save(user);

        Optional<User> found = adapter.findByEmail("persist-test@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Email User");
    }

    @Test
    @DisplayName("should save and find user by id")
    void saveAndFindById() {
        User user = User.createNew("+79002222222");
        User saved = adapter.save(user);

        Optional<User> found = adapter.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getPhone()).isEqualTo("+79002222222");
    }

    @Test
    @DisplayName("should return empty when user not found by phone")
    void findByPhone_notFound() {
        Optional<User> found = adapter.findByPhone("+70000000000");
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("should update user telegram chat id")
    void updateTelegramChatId() {
        User user = User.createNew("+79003333333");
        User saved = adapter.save(user);

        saved.setTelegramChatId(123456789L);
        adapter.save(saved);

        Optional<User> found = adapter.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTelegramChatId()).isEqualTo(123456789L);
    }
}
