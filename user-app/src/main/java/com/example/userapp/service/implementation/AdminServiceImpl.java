package com.example.userapp.service.implementation;

import com.example.common.dto.PageResponse;
import com.example.common.dto.UserDto;
import com.example.common.enums.Role;
import com.example.common.exception.CustomDuplicateFieldException;
import com.example.common.exception.UserNotFoundException;
import com.example.userapp.dto.request.admin.AdminRegisterRequest;
import com.example.userapp.dto.request.admin.AdminUpdateRequest;
import com.example.userapp.entity.User;
import com.example.userapp.exception.InvalidActionException;
import com.example.userapp.exception.AdminActionNotAllowedException;
import com.example.userapp.mapper.UserMapper;
import com.example.userapp.repository.UserRepository;
import com.example.userapp.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UUID createUser(AdminRegisterRequest request) {
        User newUser = UserMapper.mapAdminRegisterToUser(request);
        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new CustomDuplicateFieldException("Email already exists");
        }
        if (userRepository.existsByPhone(newUser.getPhone())) {
            throw new CustomDuplicateFieldException("Phone already exists");
        }
        newUser.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(newUser);
        return newUser.getId();
    }

    @Override
    @Transactional
    public UUID updateUser(UUID id, AdminUpdateRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    if (user.getRole().equals(Role.ADMIN) && request.role() != null && request.role().equals(Role.ADMIN)) {
                        throw new AdminActionNotAllowedException("Admin cannot change the role of another admin");
                    }
                    if (request.firstName() != null) user.setFirstName(request.firstName());
                    if (request.lastName() != null) user.setLastName(request.lastName());
                    if (request.role() != null) user.setRole(request.role());
                    userRepository.save(user);
                    return user.getId();
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }


    @Override
    @Transactional
    public UUID blockUser(UUID id, UserDto admin) {
        UUID adminId = admin.id();
        if (id.equals(adminId)) {
            throw new AdminActionNotAllowedException("Admin cannot block themselves");
        }

        return userRepository.findById(id)
                .map(user -> {
                    if (user.getRole().equals(Role.ADMIN)) {
                        throw new AdminActionNotAllowedException("Admin cannot block another admin");
                    }
                    if (user.isBlocked()) {
                        throw new InvalidActionException("User is already blocked");
                    }
                    user.setBlocked(true);
                    userRepository.save(user);
                    return user.getId();
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    @Override
    @Transactional
    public UUID unblockUser(UUID id, UserDto admin) {
        UUID adminId = admin.id();
        if (id.equals(adminId)) {
            throw new AdminActionNotAllowedException("Admin cannot unblock themselves");
        }

        return userRepository.findById(id)
                .map(user -> {
                    if (user.getRole().equals(Role.ADMIN)) {
                        throw new AdminActionNotAllowedException("Admin cannot unblock another admin");
                    }
                    if (!user.isBlocked()) {
                        throw new InvalidActionException("User is not blocked");
                    }
                    user.setBlocked(false);
                    userRepository.save(user);
                    return user.getId();
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    @Override
    @Transactional
    public UUID updateUserRole(UUID id, Role newRole, UserDto admin) {
        UUID adminId = admin.id();
        return userRepository.findById(id)
                .map(user -> {
                    if (id.equals(adminId)) {
                        throw new AdminActionNotAllowedException("Admin cannot change their own role");
                    }
                    if (user.getRole().equals(Role.ADMIN)) {
                        throw new AdminActionNotAllowedException("Admin cannot change the role of another admin");
                    }
                    if (user.getRole().equals(newRole)) {
                        throw new InvalidActionException("User already has this role");
                    }
                    user.setRole(newRole);
                    userRepository.save(user);
                    return user.getId();
                })
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    @Override
    public PageResponse<UserDto> findAllUsers(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        List<UserDto> users = page.getContent().stream()
                .map(UserMapper::mapUserToUserDto)
                .toList();
        return new PageResponse<>(
                users,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

}
