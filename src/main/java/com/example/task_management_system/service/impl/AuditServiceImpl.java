package com.example.task_management_system.service.impl;

import com.example.task_management_system.model.AuditLog;
import com.example.task_management_system.repository.AuditLogRepository;
import com.example.task_management_system.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void logAction(String username, String action, String details) {
        AuditLog auditLog = new AuditLog(username, action, details, new Date());
        auditLogRepository.save(auditLog);
    }
}
