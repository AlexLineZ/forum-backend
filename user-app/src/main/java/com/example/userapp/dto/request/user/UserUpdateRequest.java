package com.example.userapp.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @Size(min = 1, max = 100, message = "Name must be at least 1 character and no more than 100")
    @NotBlank(message = "User firstname cannot be empty")
    private String firstName;

    @Size(min = 1, max = 100, message = "Lastname must be at least 1 character and no more than 100")
    @NotBlank(message = "User Lastname cannot be empty")
    private String lastName;

    private Long phone;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "User password cannot be empty")
    private String password;
}
