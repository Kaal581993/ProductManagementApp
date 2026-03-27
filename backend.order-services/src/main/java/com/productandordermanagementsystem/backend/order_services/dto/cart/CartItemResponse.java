package com.productandordermanagementsystem.backend.order_services.dto.cart;

import java.math.BigDecimal;

public record CartItemResponse(
        Long cartItemId,
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal) {
}
