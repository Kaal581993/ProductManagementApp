package com.productandordermanagementsystem.backend.user_service.service;

import com.productandordermanagementsystem.backend.user_service.dto.AuthorizationRequest;
import com.productandordermanagementsystem.backend.user_service.dto.AuthorizationResponse;
import com.productandordermanagementsystem.backend.user_service.entity.Role;
import com.productandordermanagementsystem.backend.user_service.entity.User;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final CurrentUserService currentUserService;

    public AuthorizationService(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    public AuthorizationResponse evaluate(AuthorizationRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        if (currentUser.getRole() == Role.ROLE_ADMIN) {
            return new AuthorizationResponse(true, "Allowed by RBAC: admin role");
        }

        if (request.resourceOwnerId() != null && request.resourceOwnerId().equals(currentUser.getId())) {
            return new AuthorizationResponse(true, "Allowed by ABAC: resource owner");
        }

        boolean sameDepartment = request.resourceDepartment() != null && request.resourceDepartment().equalsIgnoreCase(currentUser.getDepartment());
        boolean sameRegion = request.resourceRegion() != null && request.resourceRegion().equalsIgnoreCase(currentUser.getRegion());
        boolean readAction = request.action() == null || request.action().equalsIgnoreCase("READ");

        if (readAction && sameDepartment && sameRegion) {
            return new AuthorizationResponse(true, "Allowed by ABAC: same department and region for read action");
        }

        return new AuthorizationResponse(false, "Denied by RBAC/ABAC policy");
    }
}
