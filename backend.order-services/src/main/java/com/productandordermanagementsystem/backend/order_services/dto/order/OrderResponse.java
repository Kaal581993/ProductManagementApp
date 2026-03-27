package com.productandordermanagementsystem.backend.order_services.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(Long orderId, Long userId, String customerName, String status, BigDecimal totalAmount, LocalDateTime createdAt, List<OrderItemResponse> items) {
}
