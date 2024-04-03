package com.example.userapp.dto.request;

import com.example.common.enums.Role;

public record AdminRegisterRequest(
        Long phone,
        String password,
        String firstName,
        String lastName,
        String email,
        Role role
) { }
