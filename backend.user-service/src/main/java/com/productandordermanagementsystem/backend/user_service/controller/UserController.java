package com.productandordermanagementsystem.backend.user_service.controller;

import com.productandordermanagementsystem.backend.user_service.dto.UserProfileResponse;
import com.productandordermanagementsystem.backend.user_service.dto.UserSummaryResponse;
import com.productandordermanagementsystem.backend.user_service.entity.User;
import com.productandordermanagementsystem.backend.user_service.exception.ResourceNotFoundException;
import com.productandordermanagementsystem.backend.user_service.repository.UserRepository;
import com.productandordermanagementsystem.backend.user_service.service.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;

    public UserController(CurrentUserService currentUserService, UserRepository userRepository) {
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> me() {
        User user = currentUserService.getCurrentUser();
        return ResponseEntity.ok(new UserProfileResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name(), user.getDepartment(), user.getRegion()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserSummaryResponse> getUserSummary(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok(new UserSummaryResponse(user.getId(), user.getName()));
    }
}
