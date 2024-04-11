package com.example.userapp.service;

import com.example.userapp.dto.request.admin.AdminRegisterRequest;
import com.example.userapp.dto.request.admin.AdminUpdateRequest;
import com.example.userapp.dto.request.admin.UserRoleUpdateRequest;

import java.util.UUID;

public interface AdminService {
    UUID createUser(AdminRegisterRequest request);
    UUID updateUser(UUID id, AdminUpdateRequest request);
    UUID blockUser(UUID id);
    UUID unblockUser(UUID id);
    UUID updateUserRole(UUID id, UserRoleUpdateRequest request);
}
