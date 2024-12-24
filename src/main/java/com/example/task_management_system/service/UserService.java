package com.example.task_management_system.service;

import com.example.task_management_system.dto.UserRegistrationDto;
import com.example.task_management_system.model.User;

import java.util.List;

public interface UserService {
    void registerUser(UserRegistrationDto userDto);
    User getUserByUsername(String username);

    // Admin-specific methods
    List<User> getAllUsers();
    void deleteUserById(Long id);

}
