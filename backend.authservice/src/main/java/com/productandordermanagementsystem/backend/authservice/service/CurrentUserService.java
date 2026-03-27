package com.productandordermanagementsystem.backend.authservice.service;

import com.productandordermanagementsystem.backend.authservice.entity.User;
import com.productandordermanagementsystem.backend.authservice.exception.ResourceNotFoundException;
import com.productandordermanagementsystem.backend.authservice.repository.UserRepository;
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
