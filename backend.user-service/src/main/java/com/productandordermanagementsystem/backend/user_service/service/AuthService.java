package com.productandordermanagementsystem.backend.user_service.service;

import com.productandordermanagementsystem.backend.user_service.dto.AuthResponse;
import com.productandordermanagementsystem.backend.user_service.dto.LoginRequest;
import com.productandordermanagementsystem.backend.user_service.dto.RegisterRequest;
import com.productandordermanagementsystem.backend.user_service.entity.Role;
import com.productandordermanagementsystem.backend.user_service.entity.User;
import com.productandordermanagementsystem.backend.user_service.exception.BadRequestException;
import com.productandordermanagementsystem.backend.user_service.repository.UserRepository;
import com.productandordermanagementsystem.backend.user_service.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository repository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new BadRequestException("Email is already registered");
        }
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);
        user.setDepartment(request.department());
        user.setRegion(request.region());
        user.setEnabled(true);
        User saved = repository.save(user);
        return toAuthResponse(saved);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = repository.findByEmail(request.email()).orElseThrow(() -> new BadRequestException("Invalid credentials"));
        return toAuthResponse(user);
    }

    private AuthResponse toAuthResponse(User user) {
        return new AuthResponse(jwtService.generateToken(user), "Bearer", user.getId(), user.getName(), user.getEmail(), user.getRole().name(), user.getDepartment(), user.getRegion());
    }
}
