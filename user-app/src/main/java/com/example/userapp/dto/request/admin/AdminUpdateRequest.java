package com.example.userapp.dto.request.admin;

import com.example.common.enums.Role;

public record AdminUpdateRequest(
        String firstName,
        String lastName,
        Role role
) {
}
