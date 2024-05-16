package com.example.userapp.service;

import com.example.common.dto.page.PageResponse;
import com.example.common.dto.user.UserDto;
import com.example.common.enums.Role;
import com.example.userapp.dto.request.admin.AdminRegisterRequest;
import com.example.userapp.dto.request.admin.AdminUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminService {
    UUID createUser(AdminRegisterRequest request);
    UUID updateUser(UUID id, AdminUpdateRequest request);
    UUID blockUser(UUID id, UserDto admin);
    UUID unblockUser(UUID id, UserDto admin);
    UUID updateUserRole(UUID id, Role newRole, UserDto admin);
    PageResponse<UserDto> findAllUsers(Pageable pageable);
}
