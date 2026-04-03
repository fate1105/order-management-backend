package com.fer.ordermanagement.admin.dto.user;

import com.fer.ordermanagement.auth.enums.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateUserRoleRequest {
    @NotNull
    private RoleName roleName;
}