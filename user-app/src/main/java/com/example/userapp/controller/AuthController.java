package com.example.userapp.controller;

import com.example.userapp.dto.TokenResponse;
import com.example.userapp.dto.request.user.LoginRequest;
import com.example.userapp.dto.request.user.RegisterRequest;
import com.example.userapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/auth")
@Tag(name = "Авторизация")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("login")
    @Operation(summary = "Авторизация пользователя", description = "Позволяет пользователю войти в систему")
    public ResponseEntity<TokenResponse> loginUser(@RequestBody LoginRequest body){
        return ResponseEntity.ok(userService.loginUser(body));
    }

    @PostMapping("register")
    @Operation(summary = "Регистрация пользователя", description = "Регистрирует нового пользователя")
    public ResponseEntity<TokenResponse> registerUser(@Valid @RequestBody RegisterRequest body) {
        return ResponseEntity.ok(userService.registerUser(body));
    }

    @Operation(summary = "Подтверждение регистрации", description = "Переход по ссылке активирует аккаунт пользователя")
    @RequestMapping(value="/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> confirmUserAccount(@RequestParam("token") UUID confirmationToken) {
        userService.confirmEmail(confirmationToken);
        return ResponseEntity.ok("Account successfully verified");
    }
}
