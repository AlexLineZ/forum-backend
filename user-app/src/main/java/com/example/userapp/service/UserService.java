package com.example.userapp.service;

import com.example.common.dto.UserDto;
import com.example.userapp.dto.TokenResponse;
import com.example.userapp.dto.request.user.LoginRequest;
import com.example.userapp.dto.request.user.RegisterRequest;
import com.example.userapp.dto.request.user.UserUpdateRequest;
import com.example.userapp.dto.response.UserResponse;
import com.example.userapp.entity.User;

import java.util.UUID;

public interface UserService {
    TokenResponse registerUser(RegisterRequest body);
    TokenResponse loginUser(LoginRequest body);
    UserResponse updateUser(UserDto currentUser, UserUpdateRequest updateRequest);
    UserResponse getUserByDto(UserDto userDto);
    User getUserByAuthentication(UserDto userDto);
    UserDto getUserByUserId(UUID userId);
    void confirmEmail(UUID confirmationToken);
}
