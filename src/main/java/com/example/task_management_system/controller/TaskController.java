package com.example.task_management_system.controller;

import com.example.task_management_system.model.Task;
import com.example.task_management_system.model.User;
import com.example.task_management_system.security.CustomUserDetails;
import com.example.task_management_system.service.AuditService;
import com.example.task_management_system.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final AuditService auditService;

    @Autowired
    public TaskController(TaskService taskService, AuditService auditService) {
        this.taskService = taskService;
        this.auditService = auditService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new RuntimeException("User is not authenticated");
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();

        if (user == null) {
            throw new RuntimeException("User object is null in CustomUserDetails");
        }

        task.setUser(user);
        Task createdTask = taskService.createTask(task);

        auditService.logAction(user.getUsername(), "CREATE_TASK", "Created task with title: " + task.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new RuntimeException("User is not authenticated");
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();

        if (user == null) {
            throw new RuntimeException("User object is null in CustomUserDetails");
        }

        // Log the action before deleting
        auditService.logAction(user.getUsername(), "DELETE_TASK", "Attempted to delete task with ID: " + id);

        // Perform the deletion
        taskService.deleteTask(id);

        // Log successful deletion
        auditService.logAction(user.getUsername(), "DELETE_TASK", "Successfully deleted task with ID: " + id);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/user") // Distinct path for user-specific tasks
    public ResponseEntity<List<Task>> getTasksByUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new RuntimeException("User is not authenticated");
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();

        List<Task> tasks = taskService.getTasksByUserId(user.getId());

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/filters")
    public ResponseEntity<List<Task>> getTasksByFilters(
            Authentication authentication,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new RuntimeException("User is not authenticated");
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();

        List<Task> tasks = taskService.filterTasks(user.getId(), title, category, status, priority, startDate, endDate);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getTaskSummary(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUser().getId();

        Map<String, Object> summary = taskService.getTaskSummary(userId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/report-by-category")
    public ResponseEntity<Map<String, Long>> getTaskReportByCategory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new RuntimeException("User is not authenticated");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        Map<String, Long> taskCountByCategory = taskService.getTaskCountByCategory(user.getId());
        return ResponseEntity.ok(taskCountByCategory);
    }

}