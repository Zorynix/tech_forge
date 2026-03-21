package com.techmarket.authservice.testconfig;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestBeanOverrides {

    @Bean
    public JavaMailSender javaMailSender() {
        return mock(JavaMailSender.class, invocation -> {
            if (invocation.getMethod().getName().equals("createMimeMessage")) {
                return new jakarta.mail.internet.MimeMessage((jakarta.mail.Session) null);
            }
            return null;
        });
    }
}
