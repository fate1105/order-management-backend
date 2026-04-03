package com.fer.ordermanagement.admin.controller;

import com.fer.ordermanagement.admin.controller.api.AdminUserApi;
import com.fer.ordermanagement.admin.dto.user.UpdateUserRoleRequest;
import com.fer.ordermanagement.admin.dto.user.UpdateUserStatusRequest;
import com.fer.ordermanagement.admin.dto.user.UserResponse;
import com.fer.ordermanagement.admin.service.AdminUserService;
import com.fer.ordermanagement.auth.enums.RoleName;
import com.fer.ordermanagement.auth.enums.UserStatus;
import com.fer.ordermanagement.auth.security.UserDetailsImpl;
import com.fer.ordermanagement.common.response.BaseResponse;
import com.fer.ordermanagement.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminUserController implements AdminUserApi {

    private final AdminUserService adminUserService;

    @Override
    public ResponseEntity<BaseResponse<PageResponse<UserResponse>>> getAll(
            UserStatus status, RoleName role, int page, int size) {
        var result = adminUserService.getAllUsers(status, role, PageRequest.of(page, size));
        return ResponseEntity.ok(BaseResponse.success(new PageResponse<>(result) ));
    }

    @Override
    public ResponseEntity<BaseResponse<UserResponse>> getOne(Long id) {
        return ResponseEntity.ok(BaseResponse.success(adminUserService.getUserById(id)));
    }

    @Override
    public ResponseEntity<BaseResponse<UserResponse>> updateStatus(
            Long id, @Valid UpdateUserStatusRequest req, Authentication auth) {
        return ResponseEntity.ok(BaseResponse.success(
                adminUserService.updateStatus(id, req, getAdminId(auth))));
    }

    @Override
    public ResponseEntity<BaseResponse<UserResponse>> updateRole(
            Long id, @Valid UpdateUserRoleRequest req, Authentication auth) {
        return ResponseEntity.ok(BaseResponse.success(
                adminUserService.updateRole(id, req, getAdminId(auth))));
    }

    @Override
    public ResponseEntity<BaseResponse<Void>> delete(Long id, Authentication auth) {
        adminUserService.deleteUser(id, getAdminId(auth));
        return ResponseEntity.ok(BaseResponse.success("Xóa người dùng thành công"));
    }

    private Long getAdminId(Authentication auth) {
        return ((UserDetailsImpl) auth.getPrincipal()).getId();
    }
}