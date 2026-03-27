package com.productandordermanagementsystem.backend.authservice.dto;

public record UserProfileResponse(
        Long id,
        String name,
        String email,
        String role,
        String department,
        String region
) {
}
