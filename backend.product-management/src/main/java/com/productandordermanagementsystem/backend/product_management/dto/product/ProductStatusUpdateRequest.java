package com.productandordermanagementsystem.backend.product_management.dto.product;

import jakarta.validation.constraints.NotNull;

public record ProductStatusUpdateRequest(@NotNull Boolean enabled) {
}
