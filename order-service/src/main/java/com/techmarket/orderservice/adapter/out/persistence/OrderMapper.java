package com.techmarket.orderservice.adapter.out.persistence;

import com.techmarket.orderservice.domain.model.Order;
import com.techmarket.orderservice.domain.model.OrderItem;
import com.techmarket.orderservice.domain.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public Order toDomain(OrderJpaEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(this::toDomain)
                .toList();

        return new Order(
                entity.getId(),
                entity.getUserId(),
                items,
                OrderStatus.valueOf(entity.getStatus()),
                entity.getTotalPrice(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public OrderItem toDomain(OrderItemJpaEntity entity) {
        return new OrderItem(
                entity.getProductId(),
                entity.getProductName(),
                entity.getPrice(),
                entity.getQuantity(),
                entity.getImageUrl()
        );
    }

    public OrderJpaEntity toJpaEntity(Order order) {
        OrderJpaEntity entity = OrderJpaEntity.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .status(order.getStatus().name())
                .totalPrice(order.getTotalPrice())
                .phone(order.getPhone())
                .email(order.getEmail())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();

        List<OrderItemJpaEntity> itemEntities = order.getItems().stream()
                .map(item -> toJpaEntity(item, entity))
                .toList();

        itemEntities.forEach(entity::addItem);
        return entity;
    }

    public OrderItemJpaEntity toJpaEntity(OrderItem item, OrderJpaEntity order) {
        return OrderItemJpaEntity.builder()
                .order(order)
                .productId(item.productId())
                .productName(item.productName())
                .price(item.price())
                .quantity(item.quantity())
                .imageUrl(item.imageUrl())
                .build();
    }
}
