package com.productandordermanagementsystem.backend.order_services.dto.order;

import java.math.BigDecimal;

public record OrderItemResponse(Long orderItemId, Long productId, String productName, Integer quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
}
