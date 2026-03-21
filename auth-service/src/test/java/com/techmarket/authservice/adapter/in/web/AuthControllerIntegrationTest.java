package com.techmarket.authservice.adapter.in.web;

import com.techmarket.authservice.adapter.in.web.dto.RefreshTokenRequest;
import com.techmarket.authservice.adapter.in.web.dto.RequestCodeRequest;
import com.techmarket.authservice.adapter.in.web.dto.VerifyCodeRequest;
import com.techmarket.authservice.testconfig.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class AuthControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = JsonMapper.builder().build();

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    @DisplayName("POST /api/v1/auth/request-code should return 200 for valid phone")
    void requestCode_validPhone_returns200() throws Exception {
        var request = new RequestCodeRequest("+79001234567", null);

        mockMvc.perform(post("/api/v1/auth/request-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/v1/auth/request-code should return 200 for valid email")
    void requestCode_validEmail_returns200() throws Exception {
        var request = new RequestCodeRequest(null, "test@example.com");

        mockMvc.perform(post("/api/v1/auth/request-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/v1/auth/request-code should return 400 when both empty")
    void requestCode_empty_returns400() throws Exception {
        var request = new RequestCodeRequest(null, null);

        mockMvc.perform(post("/api/v1/auth/request-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Full auth flow: request code -> verify -> get tokens")
    void fullAuthFlow_phone_success() throws Exception {
        String phone = "+79009999999";

        // Step 1: Request code
        mockMvc.perform(post("/api/v1/auth/request-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RequestCodeRequest(phone, null))))
                .andExpect(status().isOk());

        // Step 2: Get code from Redis
        String code = redisTemplate.opsForValue().get("auth:verification:" + phone);

        // Step 3: Verify code
        var verifyRequest = new VerifyCodeRequest(phone, null, code);
        String responseBody = mockMvc.perform(post("/api/v1/auth/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.expiresIn").isNumber())
                .andReturn().getResponse().getContentAsString();

        // Step 4: Refresh token
        String refreshToken = objectMapper.readTree(responseBody).get("refreshToken").asText();
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RefreshTokenRequest(refreshToken))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("POST /api/v1/auth/verify should return 400 for wrong code")
    void verify_wrongCode_returnsBadRequest() throws Exception {
        String phone = "+79008888888";

        // Request code first
        mockMvc.perform(post("/api/v1/auth/request-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RequestCodeRequest(phone, null))))
                .andExpect(status().isOk());

        // Verify with wrong code
        var verifyRequest = new VerifyCodeRequest(phone, null, "000000");
        mockMvc.perform(post("/api/v1/auth/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyRequest)))
                .andExpect(status().isBadRequest());
    }
}
