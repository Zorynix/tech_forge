package com.techmarket.orderservice.adapter.in.web;

import com.techmarket.orderservice.adapter.in.web.dto.AddToCartRequest;
import com.techmarket.orderservice.adapter.in.web.dto.CartResponse;
import com.techmarket.orderservice.application.port.in.CartUseCase;
import com.techmarket.orderservice.domain.model.Cart;
import com.techmarket.orderservice.domain.model.CartItem;
import com.techmarket.orderservice.infrastructure.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartUseCase cartUseCase;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal UserPrincipal principal) {
        Cart cart = cartUseCase.getCart(principal.getUserId());
        return ResponseEntity.ok(CartResponse.from(cart));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AddToCartRequest request) {
        CartItem item = new CartItem(
                request.productId(),
                request.productName(),
                request.price(),
                request.quantity(),
                request.imageUrl()
        );
        Cart cart = cartUseCase.addToCart(principal.getUserId(), item);
        return ResponseEntity.ok(CartResponse.from(cart));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> removeFromCart(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID productId) {
        Cart cart = cartUseCase.removeFromCart(principal.getUserId(), productId);
        return ResponseEntity.ok(CartResponse.from(cart));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserPrincipal principal) {
        cartUseCase.clearCart(principal.getUserId());
        return ResponseEntity.noContent().build();
    }
}
