package com.example.task_management_system.service;

import com.example.task_management_system.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final TaskRepository taskRepository;

    @Autowired
    public AdminService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Map<String, Long> getTasksByPriority() {
        List<Object[]> results = taskRepository.countTasksByPriority();
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0], // Priority
                        result -> (Long) result[1]    // Count
                ));
    }

    public Map<String, Long> getTasksByCategory() {
        List<Object[]> results = taskRepository.countTasksByCategory();
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0], // Category
                        result -> (Long) result[1]    // Count
                ));
    }

    public Map<String, Long> getUserTaskSummary() {
        List<Object[]> results = taskRepository.countTasksByUser();
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0], // Username
                        result -> (Long) result[1]    // Count
                ));
    }
}
