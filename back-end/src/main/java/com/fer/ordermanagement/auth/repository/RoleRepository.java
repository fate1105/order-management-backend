package com.fer.ordermanagement.auth.repository;

import com.fer.ordermanagement.auth.entity.Role;
import com.fer.ordermanagement.auth.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}