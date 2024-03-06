package com.example.userapp.dto.request;

public record LoginRequest(
        String email,
        String password
) { }
