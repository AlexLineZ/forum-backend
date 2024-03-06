package com.example.userapp.dto.request;

public record RegisterRequest(
        Long phone,
        String password,
        String firstName,
        String lastName,
        String email
) { }
