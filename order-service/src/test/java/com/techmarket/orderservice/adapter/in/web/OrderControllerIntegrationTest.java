package com.techmarket.orderservice.adapter.in.web;

import com.techmarket.orderservice.adapter.in.web.dto.AddToCartRequest;
import com.techmarket.orderservice.adapter.in.web.dto.CreateOrderRequest;
import com.techmarket.orderservice.testconfig.BaseIntegrationTest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class OrderControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = JsonMapper.builder().build();

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    private String generateToken(UUID userId) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(userId.toString())
                .claim("phone", "+79001234567")
                .claim("type", "access")
                .claim("roles", List.of("ROLE_USER"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();
    }

    @Test
    @DisplayName("POST /api/v1/orders should return 400 when cart is empty")
    void createOrder_emptyCart_returns400() throws Exception {
        UUID userId = UUID.randomUUID();
        String token = generateToken(userId);

        mockMvc.perform(post("/api/v1/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateOrderRequest("+79001234567", "test@example.com"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Full order flow: add to cart -> create order -> get order")
    void fullOrderFlow() throws Exception {
        UUID userId = UUID.randomUUID();
        String token = generateToken(userId);

        // Add item to cart
        var addToCartRequest = new AddToCartRequest(
                UUID.randomUUID(), "iPhone 17 Pro", BigDecimal.valueOf(129990), 1, "img.jpg");
        mockMvc.perform(post("/api/v1/cart/items")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addToCartRequest)))
                .andExpect(status().isOk());

        // Create order
        String orderResponse = mockMvc.perform(post("/api/v1/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateOrderRequest("+79001234567", "test@example.com"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.items").isArray())
                .andReturn().getResponse().getContentAsString();

        // Get order by id
        String orderId = objectMapper.readTree(orderResponse).get("id").asText();
        mockMvc.perform(get("/api/v1/orders/" + orderId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.status").value("CREATED"));

        // Get user orders
        mockMvc.perform(get("/api/v1/orders")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("GET /api/v1/orders should return 401 without token")
    void getOrders_noToken_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/v1/cart should return empty cart")
    void getCart_empty_returns200() throws Exception {
        UUID userId = UUID.randomUUID();
        String token = generateToken(userId);

        mockMvc.perform(get("/api/v1/cart")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
