package com.example.userapp.dto.request.admin;

import com.example.common.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUpdateRequest{
    @Size(min = 1, max = 100, message = "Name must be at least 1 character and no more than 100")
    @NotBlank(message = "User firstname cannot be empty")
    private String firstName;

    @Size(min = 1, max = 100, message = "Lastname must be at least 1 character and no more than 100")
    @NotBlank(message = "User Lastname cannot be empty")
    private String lastName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
}