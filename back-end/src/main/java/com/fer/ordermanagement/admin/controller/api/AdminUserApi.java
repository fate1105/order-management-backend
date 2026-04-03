package com.fer.ordermanagement.admin.controller.api;

import com.fer.ordermanagement.admin.dto.user.UpdateUserRoleRequest;
import com.fer.ordermanagement.admin.dto.user.UpdateUserStatusRequest;
import com.fer.ordermanagement.admin.dto.user.UserResponse;
import com.fer.ordermanagement.auth.enums.RoleName;
import com.fer.ordermanagement.auth.enums.UserStatus;
import com.fer.ordermanagement.common.response.BaseResponse;
import com.fer.ordermanagement.common.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin - Quản lý người dùng", description = "Các API quản lý người dùng dành cho admin")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/admin/users")
public interface AdminUserApi {

    @Operation(summary = "Lấy danh sách người dùng", description = "Hỗ trợ lọc theo trạng thái và vai trò")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @GetMapping
    ResponseEntity<BaseResponse<PageResponse<UserResponse>>> getAll(
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) RoleName role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "Lấy thông tin chi tiết người dùng")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm thấy người dùng"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    })
    @GetMapping("/{id}")
    ResponseEntity<BaseResponse<UserResponse>> getOne(@PathVariable Long id);

    @Operation(summary = "Cập nhật trạng thái người dùng", description = "Kích hoạt hoặc khóa tài khoản")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    })
    @PutMapping("/{id}/status")
    ResponseEntity<BaseResponse<UserResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest req,
            Authentication auth
    );

    @Operation(summary = "Cập nhật vai trò người dùng")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    })
    @PutMapping("/{id}/role")
    ResponseEntity<BaseResponse<UserResponse>> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRoleRequest req,
            Authentication auth
    );

    @Operation(summary = "Xóa người dùng")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<BaseResponse<Void>> delete(@PathVariable Long id, Authentication auth);
}