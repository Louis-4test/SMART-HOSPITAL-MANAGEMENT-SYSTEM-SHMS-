package com.hms.service.impl;

import com.hms.entity.AuditLog;
import com.hms.repository.AuditLogRepository;
import com.hms.service.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuditServiceImpl implements AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditServiceImpl.class);
    private final AuditLogRepository auditLogRepository;

    public AuditServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public AuditLog log(String action, String entityType, Long entityId, String details) {
        String performedBy = getCurrentUsername();
        return log(action, entityType, entityId, details, performedBy);
    }

    @Override
    public AuditLog log(String action, String entityType, Long entityId, String details, String performedBy) {
        AuditLog auditLog = new AuditLog(action, entityType, entityId, details, performedBy);
        AuditLog saved = auditLogRepository.save(auditLog);
        log.info("AUDIT: {} | {} | {} | {} | {}", action, entityType, entityId, details, performedBy);
        return saved;
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getName();
        }
        return "SYSTEM";
    }
}
