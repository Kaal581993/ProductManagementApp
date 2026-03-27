package com.productandordermanagementsystem.backend.product_management.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryDeductionRequest(@NotNull Long productId, @NotNull @Min(1) Integer quantity) {
}
