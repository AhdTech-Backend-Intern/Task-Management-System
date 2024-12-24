package com.example.task_management_system.service.impl;

import com.example.task_management_system.model.Task;
import com.example.task_management_system.repository.TaskRepository;
import com.example.task_management_system.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<Task> filterTasks(Long userId, String title, String category, String status, String priority, Date startDate, Date endDate) {
        return taskRepository.findByFilters(userId, title, category, status, priority, startDate, endDate);
    }

    @Override
    public Map<String, Object> getTaskSummary(Long userId) {
        List<Task> tasks = taskRepository.findByUserId(userId);

        Map<String, Long> statusCounts = tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalTasks", tasks.size());
        summary.put("statusCounts", statusCounts);

        return summary;
    }

    @Override
    public Map<String, Object> getAdminTaskSummary() {
        List<Task> allTasks = taskRepository.findAll();

        Map<String, Long> statusCounts = allTasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

        Map<String, Long> categoryCounts = allTasks.stream()
                .collect(Collectors.groupingBy(Task::getCategory, Collectors.counting()));

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalTasks", allTasks.size());
        summary.put("statusCounts", statusCounts);
        summary.put("categoryCounts", categoryCounts);

        return summary;
    }

    @Override
    public List<Task> searchAndFilterTasksForAdmin(String title, String category, String status, String priority, Date startDate, Date endDate) {
        return taskRepository.findByAdminFilters(title, category, status, priority, startDate, endDate);
    }

    @Override
    public Map<String, Long> getTaskCountByCategory(Long userId) {
        List<Object[]> results = taskRepository.countTasksByCategoryForUser(userId);
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0], // Category
                        result -> (Long) result[1]    // Count
                ));
    }
}
