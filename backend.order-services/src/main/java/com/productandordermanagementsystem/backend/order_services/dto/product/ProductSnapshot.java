package com.productandordermanagementsystem.backend.order_services.dto.product;

import java.math.BigDecimal;

public record ProductSnapshot(Long id, String name, String description, BigDecimal price, Integer quantity, boolean enabled) {
}
