package com.productandordermanagementsystem.backend.order_services.controller;

import com.productandordermanagementsystem.backend.order_services.dto.error.ApiErrorResponse;
import com.productandordermanagementsystem.backend.order_services.exception.BadRequestException;
import com.productandordermanagementsystem.backend.order_services.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(f -> f.getField(), f -> f.getDefaultMessage() == null ? "Invalid value" : f.getDefaultMessage(), (a, b) -> a));
        return build(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex) { return build(HttpStatus.NOT_FOUND, ex.getMessage(), null); }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(BadRequestException ex) { return build(HttpStatus.BAD_REQUEST, ex.getMessage(), null); }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message, Map<String, String> errors) {
        return ResponseEntity.status(status).body(new ApiErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, errors));
    }
}
