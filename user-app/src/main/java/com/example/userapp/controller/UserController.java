package com.example.userapp.controller;

import com.example.common.dto.UserDto;
import com.example.userapp.dto.TokenResponse;
import com.example.userapp.dto.request.LoginRequest;
import com.example.userapp.dto.request.RegisterRequest;
import com.example.userapp.dto.response.UserResponse;
import com.example.userapp.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("login")
    @Operation(summary = "Авторизация пользователя", description = "Позволяет пользователю войти в систему", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная авторизация"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    public ResponseEntity<TokenResponse> loginUser(@RequestBody LoginRequest body){
        return ResponseEntity.ok(userService.loginUser(body));
    }

    @PostMapping("register")
    @Operation(summary = "Регистрация пользователя", description = "Регистрирует нового пользователя", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная регистрация"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    public ResponseEntity<TokenResponse> registerUser(@RequestBody RegisterRequest body) {
        return ResponseEntity.ok(userService.registerUser(body));
    }

    @GetMapping("profile")
    @Operation(summary = "Получение профиля", description = "Получает профиль пользователя", responses = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "401", description = "Не авторизирован")
    })
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal UserDto userDto) {
        return ResponseEntity.ok(userService.getUserResponseByAuthentication(userDto));
    }

    @GetMapping("person")
    @Operation(summary = "Получение пользователя по ID", description = "Получает пользователя по его ID", responses = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserDto> getUserById(@RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(userService.getUserByUserId(userId));
    }
}