package com.example.task_management_system.controller;

import com.example.task_management_system.model.Task;
import com.example.task_management_system.model.User;
import com.example.task_management_system.repository.TaskRepository;
import com.example.task_management_system.service.AdminService;
import com.example.task_management_system.service.AuditService;
import com.example.task_management_system.service.TaskService;
import com.example.task_management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final TaskService taskService;
    private final AdminService adminService;
    private final TaskRepository taskRepository;
    private final AuditService auditService;

    @Autowired
    public AdminController(UserService userService, TaskService taskService, TaskRepository taskRepository,
                           AdminService adminService, AuditService auditService) {
        this.userService = userService;
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.adminService = adminService;
        this.auditService = auditService;
    }

    // Retrieve all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        auditService.logAction("ADMIN", "VIEW_USERS", "Admin retrieved all users.");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Delete a specific user by ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        auditService.logAction("ADMIN", "DELETE_USER", "Admin deleted user with ID: " + id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<String> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> roleUpdate) {
        String newRole = roleUpdate.get("role");
        if (newRole == null || newRole.isEmpty()) {
            return ResponseEntity.badRequest().body("Role is required");
        }

        userService.updateUserRole(id, newRole);
        auditService.logAction("ADMIN", "UPDATE_ROLE", "Admin updated role for user ID: " + id + " to role: " + newRole);
        return ResponseEntity.ok("User role updated successfully!");
    }

    @GetMapping("/tasks/summary")
    public ResponseEntity<Map<String, Object>> getAdminTaskSummary() {
        Map<String, Object> summary = taskService.getAdminTaskSummary();
        auditService.logAction("ADMIN", "VIEW_TASK_SUMMARY", "Admin viewed task summary.");
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/reports/tasks-by-status")
    public ResponseEntity<Map<String, Long>> getTasksCountByStatus() {
        List<Object[]> results = taskRepository.countTasksByStatus();
        Map<String, Long> tasksCountByStatus = new HashMap<>();

        for (Object[] result : results) {
            String status = (String) result[0];
            Long count = (Long) result[1];
            tasksCountByStatus.put(status, count);
        }

        auditService.logAction("ADMIN", "VIEW_TASKS_BY_STATUS", "Admin viewed task count by status.");
        return ResponseEntity.ok(tasksCountByStatus);
    }

    @GetMapping("/tasks-by-priority")
    public ResponseEntity<Map<String, Long>> getTasksByPriority() {
        Map<String, Long> tasksByPriority = adminService.getTasksByPriority();
        auditService.logAction("ADMIN", "VIEW_TASKS_BY_PRIORITY", "Admin viewed tasks by priority.");
        return ResponseEntity.ok(tasksByPriority);
    }

    @GetMapping("/tasks-by-category")
    public ResponseEntity<Map<String, Long>> getTasksByCategory() {
        Map<String, Long> tasksByCategory = adminService.getTasksByCategory();
        auditService.logAction("ADMIN", "VIEW_TASKS_BY_CATEGORY", "Admin viewed tasks by category.");
        return ResponseEntity.ok(tasksByCategory);
    }

    @GetMapping("/user-task-summary")
    public ResponseEntity<Map<String, Long>> getUserTaskSummary() {
        Map<String, Long> userTaskSummary = adminService.getUserTaskSummary();
        auditService.logAction("ADMIN", "VIEW_USER_TASK_SUMMARY", "Admin viewed user task summary.");
        return ResponseEntity.ok(userTaskSummary);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        List<Task> tasks = taskService.searchAndFilterTasksForAdmin(title, category, status, priority, startDate, endDate);
        auditService.logAction("ADMIN", "VIEW_ALL_TASKS", "Admin viewed tasks with filters applied.");
        return ResponseEntity.ok(tasks);
    }
}
