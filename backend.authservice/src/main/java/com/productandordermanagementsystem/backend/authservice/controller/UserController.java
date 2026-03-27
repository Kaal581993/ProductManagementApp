package com.productandordermanagementsystem.backend.authservice.controller;

import com.productandordermanagementsystem.backend.authservice.dto.UserProfileResponse;
import com.productandordermanagementsystem.backend.authservice.entity.User;
import com.productandordermanagementsystem.backend.authservice.service.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CurrentUserService currentUserService;

    public UserController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> me() {
        User user = currentUserService.getCurrentUser();
        return ResponseEntity.ok(new UserProfileResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name(), user.getDepartment(), user.getRegion()));
    }
}
