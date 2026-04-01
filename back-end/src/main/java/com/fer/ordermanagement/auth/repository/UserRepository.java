package com.fer.ordermanagement.auth.repository;

import com.fer.ordermanagement.auth.entity.User;
import com.fer.ordermanagement.auth.enums.RoleName;
import com.fer.ordermanagement.auth.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Cho admin filter
    Page<User> findByStatus(UserStatus status, Pageable pageable);
    Page<User> findByRole_Name(RoleName roleName, Pageable pageable);
    Page<User> findByStatusAndRole_Name(UserStatus status, RoleName roleName, Pageable pageable);
}