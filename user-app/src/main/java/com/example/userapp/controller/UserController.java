package com.example.userapp.controller;

import com.example.common.dto.UserDto;
import com.example.userapp.dto.TokenResponse;
import com.example.userapp.dto.request.user.LoginRequest;
import com.example.userapp.dto.request.user.RegisterRequest;
import com.example.userapp.dto.request.user.UserUpdateRequest;
import com.example.userapp.dto.response.UserResponse;
import com.example.userapp.entity.User;
import com.example.userapp.service.implementation.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/users")
@Tag(name = "Пользователь")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("profile")
    @Operation(summary = "Получение профиля", description = "Получает профиль пользователя")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal UserDto userDto) {
        return ResponseEntity.ok(userService.getUserByDto(userDto));
    }

    @PutMapping("profile")
    @Operation(summary = "Обновление профиля пользователя", description = "Обновляет данные профиля пользователя")
    public ResponseEntity<UserResponse> updateUser(
            @AuthenticationPrincipal UserDto userDto,
            @Valid @RequestBody UserUpdateRequest updateRequest
    ) {
        return ResponseEntity.ok(userService.updateUser(userDto, updateRequest));
    }

    @GetMapping("person")
    @Operation(summary = "Получение пользователя по ID", description = "Получает пользователя по его ID")
    public ResponseEntity<UserDto> getUserById(@RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(userService.getUserByUserId(userId));
    }
}