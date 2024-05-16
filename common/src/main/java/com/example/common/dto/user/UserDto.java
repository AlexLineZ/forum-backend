package com.example.common.dto.user;

import com.example.common.enums.Role;

import java.util.Date;
import java.util.UUID;

public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Long phone,
        Date registrationDate,
        Date lastUpdateDate,
        Boolean isEnabled,
        Boolean isBlocked,
        Role role
) { }

