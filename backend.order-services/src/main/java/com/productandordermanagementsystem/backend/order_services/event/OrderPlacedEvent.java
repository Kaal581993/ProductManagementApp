package com.productandordermanagementsystem.backend.order_services.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderPlacedEvent(Long orderId, Long userId, BigDecimal totalAmount, LocalDateTime createdAt, List<OrderPlacedItem> items) {
    public record OrderPlacedItem(Long productId, String productName, Integer quantity, BigDecimal lineTotal) {
    }
}
