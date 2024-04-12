package com.example.userapp.controller;

import com.example.common.dto.UserDto;
import com.example.common.enums.Role;
import com.example.userapp.dto.request.admin.AdminRegisterRequest;
import com.example.userapp.dto.request.admin.AdminUpdateRequest;
import com.example.userapp.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/admin")
@Tag(name = "Администратор")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/user")
    public ResponseEntity<UUID> createUser(
            @RequestBody AdminRegisterRequest user
    ) {
        return ResponseEntity.ok(adminService.createUser(user));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UUID> updateUser(
            @PathVariable UUID id,
            @RequestBody AdminUpdateRequest user
    ) {
        return ResponseEntity.ok(adminService.updateUser(id, user));
    }

    @PatchMapping("/user/{id}/role")
    public ResponseEntity<UUID> updateUserRole(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "USER") Role role,
            @AuthenticationPrincipal UserDto admin
    ) {
        return ResponseEntity.ok(adminService.updateUserRole(id, role, admin));
    }

    //не делал удаление пользователя, иначе пришлось бы удалять все созданные сообщения и тд, поэтому оставил только бан
    @PatchMapping("/user/{id}/block")
    public ResponseEntity<UUID> blockUser(@PathVariable UUID id, @AuthenticationPrincipal UserDto admin) {
        return ResponseEntity.ok(adminService.blockUser(id, admin));
    }

    @PatchMapping("/user/{id}/unblock")
    public ResponseEntity<UUID> unblockUser(@PathVariable UUID id, @AuthenticationPrincipal UserDto admin) {
        return ResponseEntity.ok(adminService.unblockUser(id, admin));
    }
}
