package com.productandordermanagementsystem.backend.user_service.dto;

public record UserProfileResponse(
        Long id,
        String name,
        String email,
        String role,
        String department,
        String region) {
}
