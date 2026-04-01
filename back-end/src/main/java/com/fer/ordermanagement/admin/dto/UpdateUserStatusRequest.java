package com.fer.ordermanagement.admin.dto;

import com.fer.ordermanagement.auth.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateUserStatusRequest {
    @NotNull
    private UserStatus status;
}