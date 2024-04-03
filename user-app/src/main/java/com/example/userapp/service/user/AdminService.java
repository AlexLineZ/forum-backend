package com.example.userapp.service.user;

import com.example.common.exception.UserNotFoundException;
import com.example.userapp.dto.request.AdminRegisterRequest;
import com.example.userapp.dto.request.AdminUpdateRequest;
import com.example.userapp.entity.User;
import com.example.userapp.mapper.UserMapper;
import com.example.userapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UUID createUser(AdminRegisterRequest request) {
        User newUser = UserMapper.mapAdminRegisterToUser(request);
        newUser.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(newUser);
        return newUser.getId();
    }

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

    public UUID blockUser(UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setBlocked(true);
                    userRepository.save(user);
                    return user.getId();
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    public UUID unblockUser(UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setBlocked(false);
                    userRepository.save(user);
                    return user.getId();
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }
}
