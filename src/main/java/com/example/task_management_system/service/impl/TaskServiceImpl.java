package com.example.task_management_system.service.impl;

import com.example.task_management_system.model.Task;
import com.example.task_management_system.repository.TaskRepository;
import com.example.task_management_system.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(Task task) {
        System.out.println("Saving task: " + task);
        Task savedTask = taskRepository.save(task);
        System.out.println("Saved task: " + savedTask);
        return savedTask;
    }

    @Override
    public Task updateTask(Long taskId, Task task) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setCategory(task.getCategory());
        existingTask.setPriority(task.getPriority());
        existingTask.setStatus(task.getStatus());
        existingTask.setDueDate(task.getDueDate());
        return taskRepository.save(existingTask);
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
    }

    @Override
    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public List<Task> searchTasksByTitle(Long userId, String title) {
        return taskRepository.findByUserIdAndTitleContainingIgnoreCase(userId, title);
    }

    @Override
    public List<Task> filterTasksByCategory(Long userId, String category) {
        return taskRepository.findByUserIdAndCategoryContainingIgnoreCase(userId, category);
    }

    @Override
    public List<Task> filterTasksByStatus(Long userId, String status) {
        return taskRepository.findByUserIdAndStatus(userId, status);
    }

    @Override
    public List<Task> filterTasksByDateRange(Long userId, Date startDate, Date endDate) {
        return taskRepository.findByUserIdAndDueDateBetween(userId, startDate, endDate);
    }

    @Override
    public List<Task> filterTasksByPriority(Long userId, String priority) {
        return taskRepository.findByUserIdAndPriority(userId, priority);
    }
}
