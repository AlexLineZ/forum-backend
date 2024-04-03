package com.example.userapp.dto.request;

import com.example.common.enums.Role;

public record AdminUpdateRequest(
        String firstName,
        String lastName,
        Role role
) {
}
