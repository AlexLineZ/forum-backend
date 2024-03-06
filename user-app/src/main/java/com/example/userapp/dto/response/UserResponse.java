package com.example.userapp.dto.response;

import java.util.Date;

public record UserResponse(
        String id,
        String firstName,
        String lastName,
        String fullName,
        String email,
        Long phone,
        Date registrationDate,
        Date lastUpdateDate
) { }
