package com.productandordermanagementsystem.backend.product_management.dto.product;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, String description, BigDecimal price, Integer quantity, boolean enabled) {
}
