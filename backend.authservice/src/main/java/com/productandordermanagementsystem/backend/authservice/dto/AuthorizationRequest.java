package com.productandordermanagementsystem.backend.authservice.dto;

public record AuthorizationRequest(
        Long resourceOwnerId,
        String resourceDepartment,
        String resourceRegion,
        String action
) {
}
