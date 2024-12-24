package com.example.task_management_system.controller;

import com.example.task_management_system.model.Task;
import com.example.task_management_system.model.User;
import com.example.task_management_system.security.CustomUserDetails;
import com.example.task_management_system.service.TaskService;
import com.example.task_management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
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

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
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
            @RequestParam(required = false) String priority, // New priority parameter
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new RuntimeException("User is not authenticated");
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();

        List<Task> tasks;

        if (title != null) {
            tasks = taskService.searchTasksByTitle(user.getId(), title);
        } else if (category != null) {
            tasks = taskService.filterTasksByCategory(user.getId(), category);
        } else if (status != null) {
            tasks = taskService.filterTasksByStatus(user.getId(), status);
        } else if (priority != null) { // Handle priority filter
            tasks = taskService.filterTasksByPriority(user.getId(), priority);
        } else if (startDate != null && endDate != null) {
            tasks = taskService.filterTasksByDateRange(user.getId(), startDate, endDate);
        } else {
            tasks = taskService.getTasksByUserId(user.getId());
        }

        return ResponseEntity.ok(tasks);
    }

}