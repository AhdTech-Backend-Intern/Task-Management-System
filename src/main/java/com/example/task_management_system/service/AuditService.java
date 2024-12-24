package com.example.task_management_system.service;

import com.example.task_management_system.model.AuditLog;

public interface AuditService {
    void logAction(String username, String action, String details);
}
