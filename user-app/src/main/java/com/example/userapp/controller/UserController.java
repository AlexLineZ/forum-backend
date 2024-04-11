package com.example.userapp.controller;

import com.example.common.dto.UserDto;
import com.example.userapp.dto.TokenResponse;
import com.example.userapp.dto.request.user.LoginRequest;
import com.example.userapp.dto.request.user.RegisterRequest;
import com.example.userapp.dto.response.UserResponse;
import com.example.userapp.service.implementation.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
        return ResponseEntity.ok(userService.getUserByDto(userDto));
    }

    @GetMapping("person")
    @Operation(summary = "Получение пользователя по ID", description = "Получает пользователя по его ID", responses = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<UserDto> getUserById(@RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(userService.getUserByUserId(userId));
    }

    @RequestMapping(value="/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Void> confirmUserAccount(@RequestParam("token") UUID confirmationToken) {
        userService.confirmEmail(confirmationToken);
        return ResponseEntity.ok().build();
    }
}