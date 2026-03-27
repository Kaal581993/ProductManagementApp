package com.productandordermanagementsystem.backend.order_services.dto.product;

public record InventoryDeductionRequest(Long productId, Integer quantity) {
}
