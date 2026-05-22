package com.productandordermanagementsystem.backend.order_services.security;

public record AuthenticatedUser(
        Long userId,
        String email,
        String role) {
}
