package com.fer.ordermanagement.admin.controller;

import com.fer.ordermanagement.admin.dto.user.UpdateUserRoleRequest;
import com.fer.ordermanagement.admin.dto.user.UpdateUserStatusRequest;
import com.fer.ordermanagement.admin.dto.user.UserResponse;
import com.fer.ordermanagement.admin.service.AdminUserService;
import com.fer.ordermanagement.auth.enums.RoleName;
import com.fer.ordermanagement.auth.enums.UserStatus;
import com.fer.ordermanagement.auth.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAll(
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) RoleName role,
            Pageable pageable) {
        return ResponseEntity.ok(adminUserService.getAllUsers(status, role, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.getUserById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<UserResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserStatusRequest req,
            Authentication auth) {
        return ResponseEntity.ok(adminUserService.updateStatus(id, req, getAdminId(auth)));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateRole(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserRoleRequest req,
            Authentication auth) {
        return ResponseEntity.ok(adminUserService.updateRole(id, req, getAdminId(auth)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        adminUserService.deleteUser(id, getAdminId(auth));
        return ResponseEntity.noContent().build();
    }

    private Long getAdminId(Authentication auth) {
        return ((UserDetailsImpl) auth.getPrincipal()).getId();
    }
}
