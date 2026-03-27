package com.productandordermanagementsystem.backend.order_services.dto.error;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> validationErrors) {
}
