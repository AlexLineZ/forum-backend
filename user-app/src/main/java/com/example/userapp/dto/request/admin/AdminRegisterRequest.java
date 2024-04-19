package com.example.userapp.dto.request.admin;

import com.example.common.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminRegisterRequest{
    @Size(min = 5, max = 30, message = "Email must be at least 5 character and no more than 30")
    @NotBlank(message = "User email cannot be empty")
    @Email(message = "Invalid email")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "User password cannot be empty")
    private String password;

    @Size(min = 1, max = 100, message = "Name must be at least 1 character and no more than 100")
    @NotBlank(message = "User firstname cannot be empty")
    private String firstName;

    @Size(min = 1, max = 100, message = "Lastname must be at least 1 character and no more than 100")
    @NotBlank(message = "User Lastname cannot be empty")
    private String lastName;

    private Long phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
}