package com.example.task_management_system.service;

import com.example.task_management_system.dto.UserRegistrationDto;

public interface UserService {
    void registerUser(UserRegistrationDto userDto);
}
