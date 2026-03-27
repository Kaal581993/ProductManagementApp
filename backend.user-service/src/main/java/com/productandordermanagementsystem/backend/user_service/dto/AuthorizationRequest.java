package com.productandordermanagementsystem.backend.user_service.dto;

public record AuthorizationRequest(
        Long resourceOwnerId,
        String resourceDepartment,
        String resourceRegion,
        String action
) {
}
