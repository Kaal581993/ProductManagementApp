package com.productandordermanagementsystem.backend.user_service.exception;

import com.productandordermanagementsystem.backend.user_service.dto.ApiErrorResponse;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler({BadRequestException.class, BadCredentialsException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequest(RuntimeException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "Access denied", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message, Map<String, String> validationErrors) {
        return ResponseEntity.status(status).body(new ApiErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, validationErrors));
    }
}
