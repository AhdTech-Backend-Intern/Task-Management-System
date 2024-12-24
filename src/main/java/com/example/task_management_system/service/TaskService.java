package com.example.task_management_system.service;

import com.example.task_management_system.model.Task;

import java.util.Date;
import java.util.List;

public interface TaskService {
    Task createTask(Task task);
    Task updateTask(Long taskId, Task task);
    void deleteTask(Long taskId);
    Task getTaskById(Long taskId);
    List<Task> getTasksByUserId(Long userId);

    // New methods for filtering
    List<Task> searchTasksByTitle(Long userId, String title);
    List<Task> filterTasksByCategory(Long userId, String category);
    List<Task> filterTasksByStatus(Long userId, String status);
    List<Task> filterTasksByDateRange(Long userId, Date startDate, Date endDate);
    List<Task> filterTasksByPriority(Long userId, String priority);

}
