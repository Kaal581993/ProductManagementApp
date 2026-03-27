package com.productandordermanagementsystem.backend.authservice.dto;

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
