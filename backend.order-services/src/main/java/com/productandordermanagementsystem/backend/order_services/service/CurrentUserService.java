package com.productandordermanagementsystem.backend.order_services.service;

import com.productandordermanagementsystem.backend.order_services.security.AuthenticatedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    public AuthenticatedUser currentUser() {
        return (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
