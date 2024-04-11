package com.example.userapp.service.implementation;

import com.example.common.exception.UserNotFoundException;
import com.example.userapp.dto.request.admin.AdminRegisterRequest;
import com.example.userapp.dto.request.admin.AdminUpdateRequest;
import com.example.userapp.dto.request.admin.UserRoleUpdateRequest;
import com.example.userapp.entity.User;
import com.example.userapp.mapper.UserMapper;
import com.example.userapp.repository.UserRepository;
import com.example.userapp.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UUID createUser(AdminRegisterRequest request) {
        User newUser = UserMapper.mapAdminRegisterToUser(request);
        newUser.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(newUser);
        return newUser.getId();
    }

    @Override
    public UUID updateUser(UUID id, AdminUpdateRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    if (request.firstName() != null) user.setFirstName(request.firstName());
                    if (request.lastName() != null) user.setLastName(request.lastName());
                    if (request.role() != null) user.setRole(request.role());
                    userRepository.save(user);
                    return user.getId();
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    @Override
    public UUID blockUser(UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setBlocked(true);
                    userRepository.save(user);
                    return user.getId();
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    @Override
    public UUID unblockUser(UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setBlocked(false);
                    userRepository.save(user);
                    return user.getId();
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    @Override
    public UUID updateUserRole(UUID id, UserRoleUpdateRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setRole(request.role());
                    userRepository.save(user);
                    return user.getId();
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }
}
