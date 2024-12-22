package com.example.task_management_system.service;

import com.example.task_management_system.model.Task;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);
    Task updateTask(Long taskId, Task task);
    void deleteTask(Long taskId);
    Task getTaskById(Long taskId);
    List<Task> getTasksByUserId(Long userId);
}
