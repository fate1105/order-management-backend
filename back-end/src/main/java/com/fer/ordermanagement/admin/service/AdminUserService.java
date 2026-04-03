package com.fer.ordermanagement.admin.service;

import com.fer.ordermanagement.admin.dto.user.UpdateUserRoleRequest;
import com.fer.ordermanagement.admin.dto.user.UpdateUserStatusRequest;
import com.fer.ordermanagement.admin.dto.user.UserResponse;
import com.fer.ordermanagement.audit.service.AuditLogService;
import com.fer.ordermanagement.auth.entity.Role;
import com.fer.ordermanagement.auth.entity.User;
import com.fer.ordermanagement.auth.enums.RoleName;
import com.fer.ordermanagement.auth.enums.UserStatus;
import com.fer.ordermanagement.auth.repository.RoleRepository;
import com.fer.ordermanagement.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuditLogService auditLogService;

    public Page<UserResponse> getAllUsers(UserStatus status, RoleName roleName, Pageable pageable) {
        if (status != null && roleName != null)
            return userRepository.findByStatusAndRole_Name(status, roleName, pageable).map(this::toResponse);
        if (status != null)
            return userRepository.findByStatus(status, pageable).map(this::toResponse);
        if (roleName != null)
            return userRepository.findByRole_Name(roleName, pageable).map(this::toResponse);
        return userRepository.findAll(pageable).map(this::toResponse);
    }

    public UserResponse getUserById(Long id) {
        return toResponse(findUser(id));
    }

    @Transactional
    public UserResponse updateStatus(Long id, UpdateUserStatusRequest req, Long adminId) {
        User user = findUser(id);
        user.setStatus(req.getStatus());
        userRepository.save(user);
        auditLogService.log(adminId, "UPDATE_STATUS", "USER", id);
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateRole(Long id, UpdateUserRoleRequest req, Long adminId) {
        User user = findUser(id);
        Role role = roleRepository.findByName(req.getRoleName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        user.setRole(role);
        userRepository.save(user);
        auditLogService.log(adminId, "UPDATE_ROLE", "USER", id);
        return toResponse(user);
    }

    @Transactional
    public void deleteUser(Long id, Long adminId) {
        User user = findUser(id);
        userRepository.delete(user);
        auditLogService.log(adminId, "DELETE", "USER", id);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private UserResponse toResponse(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .fullName(u.getFullName())
                .phone(u.getPhone())
                .role(u.getRole().getName())
                .status(u.getStatus())
                .createdAt(u.getCreatedAt())
                .build();
    }
}