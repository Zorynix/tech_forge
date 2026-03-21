package com.techmarket.authservice.adapter.out;

import com.techmarket.authservice.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RedisVerificationCodeStoreIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RedisVerificationCodeStore store;

    @Test
    @DisplayName("should save and find verification code")
    void saveAndFind() {
        store.save("+79001234567", "123456", Duration.ofMinutes(5));

        Optional<String> code = store.find("+79001234567");

        assertThat(code).isPresent().contains("123456");
    }

    @Test
    @DisplayName("should delete verification code")
    void delete() {
        store.save("+79009876543", "654321", Duration.ofMinutes(5));
        store.delete("+79009876543");

        Optional<String> code = store.find("+79009876543");

        assertThat(code).isEmpty();
    }

    @Test
    @DisplayName("should return empty for non-existent key")
    void find_notFound() {
        Optional<String> code = store.find("+70000000000");

        assertThat(code).isEmpty();
    }

    @Test
    @DisplayName("should expire code after TTL")
    void codeExpires() throws InterruptedException {
        store.save("+79005555555", "111111", Duration.ofSeconds(1));
        Thread.sleep(1500);

        Optional<String> code = store.find("+79005555555");

        assertThat(code).isEmpty();
    }
}
