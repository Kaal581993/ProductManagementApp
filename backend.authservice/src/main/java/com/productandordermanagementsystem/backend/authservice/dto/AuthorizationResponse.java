package com.productandordermanagementsystem.backend.authservice.dto;

public record AuthorizationResponse(
        boolean allowed,
        String reason
    ) {
}
