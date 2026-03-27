package com.productandordermanagementsystem.backend.authservice.controller;

import com.productandordermanagementsystem.backend.authservice.dto.AuthorizationRequest;
import com.productandordermanagementsystem.backend.authservice.dto.AuthorizationResponse;
import com.productandordermanagementsystem.backend.authservice.service.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authorization")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/check")
    public ResponseEntity<AuthorizationResponse> check(@RequestBody AuthorizationRequest request) {
        return ResponseEntity.ok(authorizationService.evaluate(request));
    }
}
