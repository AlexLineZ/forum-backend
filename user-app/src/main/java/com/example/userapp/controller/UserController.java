package com.example.userapp.controller;

import com.example.userapp.dto.TokenResponse;
import com.example.userapp.dto.request.LoginRequest;
import com.example.userapp.dto.request.RegisterRequest;
import com.example.userapp.dto.response.UserResponse;
import com.example.userapp.entity.User;
import com.example.userapp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("login")
    public ResponseEntity<TokenResponse> loginUser(@RequestBody LoginRequest body){
        return ResponseEntity.ok(userService.loginUser(body));
    }

    @PostMapping("register")
    public ResponseEntity<TokenResponse> registerUser(@RequestBody RegisterRequest body) {
        return ResponseEntity.ok(userService.registerUser(body));
    }

    @GetMapping("profile")
    public ResponseEntity<UserResponse> getUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserResponseByAuthentication(authentication));
    }
}