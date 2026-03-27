package com.productandordermanagementsystem.backend.user_service.dto;

public record AuthorizationResponse(
        boolean allowed, String reason
) {
}
