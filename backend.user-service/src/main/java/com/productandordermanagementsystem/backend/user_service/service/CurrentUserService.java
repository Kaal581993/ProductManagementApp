package com.productandordermanagementsystem.backend.user_service.service;

import com.productandordermanagementsystem.backend.user_service.entity.User;
import com.productandordermanagementsystem.backend.user_service.exception.ResourceNotFoundException;
import com.productandordermanagementsystem.backend.user_service.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository repository;

    public CurrentUserService(UserRepository repository) {
        this.repository = repository;
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
