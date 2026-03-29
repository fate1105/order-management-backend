package com.fer.ordermanagement.auth.controller.api;

import com.fer.ordermanagement.auth.dto.AuthResponse;
import com.fer.ordermanagement.auth.dto.LoginRequest;
import com.fer.ordermanagement.auth.dto.RegisterRequest;
import com.fer.ordermanagement.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Auth", description = "Xác thực người dùng")
@RequestMapping("/auth")
public interface AuthApi {

    @Operation(summary = "Đăng nhập")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Đăng nhập thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "401", description = "Sai tài khoản hoặc mật khẩu")
    })
    @PostMapping("/login")
    ResponseEntity<BaseResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    );

    @Operation(summary = "Đăng ký tài khoản")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Đăng ký thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "409", description = "Tài khoản đã tồn tại")
    })
    @PostMapping("/register")
    ResponseEntity<BaseResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request
    );
}