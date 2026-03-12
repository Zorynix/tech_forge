package com.techmarket.orderservice.adapter.in.web;

import com.techmarket.orderservice.adapter.in.web.dto.*;
import com.techmarket.orderservice.application.port.in.CreateOrderUseCase;
import com.techmarket.orderservice.application.port.in.GetOrderUseCase;
import com.techmarket.orderservice.application.port.in.UpdateOrderStatusUseCase;
import com.techmarket.orderservice.domain.model.Order;
import com.techmarket.orderservice.infrastructure.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreateOrderRequest request) {
        Order order = createOrderUseCase.createOrder(principal.getUserId(), request.phone(), request.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.from(order));
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrderResponse>> getMyOrders(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Order> page = getOrderUseCase.getByUserId(principal.getUserId(), pageable);
        return ResponseEntity.ok(PageResponse.from(page, OrderResponse::from));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID id) {
        Order order = getOrderUseCase.getById(id);
        return ResponseEntity.ok(OrderResponse.from(order));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        Order order = updateOrderStatusUseCase.updateStatus(id, request.status());
        return ResponseEntity.ok(OrderResponse.from(order));
    }
}
