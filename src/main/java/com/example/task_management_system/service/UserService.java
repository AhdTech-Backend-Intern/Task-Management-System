package com.example.task_management_system.service;

import com.example.task_management_system.dto.UserRegistrationDto;
import com.example.task_management_system.model.User;

public interface UserService {
    void registerUser(UserRegistrationDto userDto);
    User getUserByUsername(String username);

}
