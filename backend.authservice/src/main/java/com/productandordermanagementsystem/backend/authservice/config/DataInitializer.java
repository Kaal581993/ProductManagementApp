package com.productandordermanagementsystem.backend.authservice.config;

import com.productandordermanagementsystem.backend.authservice.entity.Role;
import com.productandordermanagementsystem.backend.authservice.entity.User;
import com.productandordermanagementsystem.backend.authservice.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public ApplicationRunner adminInitializer(AdminProperties adminProperties, UserRepository repository, PasswordEncoder encoder) {
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
