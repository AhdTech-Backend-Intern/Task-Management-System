package com.example.task_management_system.controller;

import com.example.task_management_system.model.User;
import com.example.task_management_system.security.CustomUserDetails;
import com.example.task_management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/me")
    public ResponseEntity<User> getLoggedInUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(401).build(); // Return 401 if no authenticated user
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User loggedInUser = customUserDetails.getUser();

        return ResponseEntity.ok(loggedInUser); // Return the user profile
    }

    @PutMapping("/me")
    public ResponseEntity<String> updateUserProfile(@RequestBody Map<String, String> updates, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getUser().getId();

        if (updates.containsKey("role")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot update your role");
        }

        userService.updateUserProfile(userId, updates);
        return ResponseEntity.ok("Profile updated successfully!");
    }

}
