package com.example.task_management_system.controller;

import com.example.task_management_system.model.User;
import com.example.task_management_system.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

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
}
