package com.example.userapp.mapper;

import com.example.common.dto.UserDto;
import com.example.userapp.dto.request.admin.AdminRegisterRequest;
import com.example.userapp.dto.request.user.RegisterRequest;
import com.example.userapp.dto.response.UserResponse;
import com.example.userapp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static User mapRegisterBodyToUser(RegisterRequest body) {
        User user = new User();
        user.setPassword(body.getPassword());
        user.setFirstName(body.getFirstName());
        user.setLastName(body.getLastName());
        user.setEmail(body.getEmail());
        user.setPhone(body.getPhone());
        return user;
    }

    public static UserResponse mapUserToResponse(User user) {
        return new UserResponse(
                user.getId().toString(),
                user.getFirstName(),
                user.getLastName(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRegistrationDate(),
                user.getLastUpdateDate()
        );
    }
    public static UserDto userToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRegistrationDate(),
                user.getLastUpdateDate(),
                user.isEnabled(),
                user.isBlocked(),
                user.getRole()
        );
    }

    public static User mapAdminRegisterToUser(AdminRegisterRequest request) {
        User user = new User();
        user.setPhone(request.phone());
        user.setPassword(request.password());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setRole(request.role());
        user.setEnabled(true);
        user.setBlocked(false);
        return user;
    }
}
