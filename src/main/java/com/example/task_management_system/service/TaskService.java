package com.example.task_management_system.service;

import com.example.task_management_system.model.Task;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TaskService {
    Task createTask(Task task);
    Task updateTask(Long taskId, Task task);
    void deleteTask(Long taskId);
    Task getTaskById(Long taskId);
    List<Task> getTasksByUserId(Long userId);
    List<Task> filterTasks(Long userId, String title, String category, String status, String priority, Date startDate, Date endDate);
    Map<String, Object> getTaskSummary(Long userId);
    Map<String, Object> getAdminTaskSummary();
    List<Task> searchAndFilterTasksForAdmin(String title, String category, String status, String priority, Date startDate, Date endDate);
    Map<String, Long> getTaskCountByCategory(Long userId);

}
