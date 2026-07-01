package com.hms.service;

import com.hms.entity.AuditLog;

public interface AuditService {
    AuditLog log(String action, String entityType, Long entityId, String details);
    AuditLog log(String action, String entityType, Long entityId, String details, String performedBy);
}
