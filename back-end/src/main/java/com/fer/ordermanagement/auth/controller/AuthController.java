package com.fer.ordermanagement.auth.controller;

import com.fer.ordermanagement.auth.controller.api.AuthApi;
import com.fer.ordermanagement.auth.dto.AuthResponse;
import com.fer.ordermanagement.auth.dto.LoginRequest;
import com.fer.ordermanagement.auth.dto.RegisterRequest;
import com.fer.ordermanagement.auth.service.AuthService;
import com.fer.ordermanagement.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<BaseResponse<AuthResponse>> login(LoginRequest request) {
        return ResponseEntity.ok(
                BaseResponse.success(authService.login(request))
        );
    }

    @Override
    public ResponseEntity<BaseResponse<AuthResponse>> register(RegisterRequest request) {
        return ResponseEntity.ok(
                BaseResponse.success(authService.register(request))
        );
    }
}