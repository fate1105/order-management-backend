package com.fer.ordermanagement.audit.service;

import com.fer.ordermanagement.audit.entity.AuditLog;
import com.fer.ordermanagement.audit.repository.AuditLogRepository;
import com.fer.ordermanagement.auth.repository.UserRepository;
import com.fer.ordermanagement.auth.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    // Dùng cho các service không có userId (tự lấy từ SecurityContext)
    public void log(String action, String entity, Long entityId) {
        Long userId = extractCurrentUserId();
        saveLog(userId, action, entity, entityId);
    }

    // Dùng cho AdminUserService đã có adminId sẵn
    public void log(Long userId, String action, String entity, Long entityId) {
        saveLog(userId, action, entity, entityId);
    }

    private void saveLog(Long userId, String action, String entity, Long entityId) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntity(entity);
        log.setEntityId(entityId);
        if (userId != null) {
            userRepository.findById(userId).ifPresent(log::setUser);
        }
        auditLogRepository.save(log);
    }

    private Long extractCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl ud) {
            return ud.getId();
        }
        return null; // anonymous (register chẳng hạn)
    }
}