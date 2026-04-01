package com.fer.ordermanagement.audit.repository;

import com.fer.ordermanagement.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}