package com.productandordermanagementsystem.backend.user_service.dto;

public record AuthResponse(
        String token,
        String tokenType,
        Long userId,
        String name,
        String email,
        String role,
        String department,
        String region
) {
}
