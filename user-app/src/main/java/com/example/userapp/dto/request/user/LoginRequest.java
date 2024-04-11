package com.example.userapp.dto.request.user;

public record LoginRequest(
        String email,
        String password
) { }
