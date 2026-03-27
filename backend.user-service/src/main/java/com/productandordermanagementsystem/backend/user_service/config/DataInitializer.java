package com.productandordermanagementsystem.backend.user_service.config;

import com.productandordermanagementsystem.backend.user_service.entity.Role;
import com.productandordermanagementsystem.backend.user_service.entity.User;
import com.productandordermanagementsystem.backend.user_service.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public ApplicationRunner adminInitializer(
            AdminProperties adminProperties,
            UserRepository repository,
            PasswordEncoder encoder) {
        return args -> {
            if (repository.existsByEmail(adminProperties.email())) {
                return;
            }
            User admin = new User();
            admin.setName(adminProperties.name());
            admin.setEmail(adminProperties.email());
            admin.setPassword(encoder.encode(adminProperties.password()));
            admin.setRole(Role.ROLE_ADMIN);
            admin.setDepartment(adminProperties.department());
            admin.setRegion(adminProperties.region());
            admin.setEnabled(true);
            repository.save(admin);
        };
    }
}
