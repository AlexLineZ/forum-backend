package com.example.userapp.dto.response;

import com.example.common.enums.Role;

import java.util.Date;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Long phone,
        Date registrationDate,
        Date lastUpdateDate,
        Role role
) { }
