package com.example.userapp.controller;

import com.example.userapp.dto.request.AdminRegisterRequest;
import com.example.userapp.dto.request.AdminUpdateRequest;
import com.example.userapp.service.user.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/admin")
@Tag(name = "Администратор")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/user")
    public ResponseEntity<UUID> createUser(@RequestBody AdminRegisterRequest user) {
        return ResponseEntity.ok(adminService.createUser(user));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UUID> updateUser(@PathVariable UUID id, @RequestBody AdminUpdateRequest user) {
        return ResponseEntity.ok(adminService.updateUser(id, user));
    }

    @PatchMapping("/user/{id}/block")
    public ResponseEntity<UUID> blockUser(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.blockUser(id));
    }

    @PatchMapping("/user/{id}/unblock")
    public ResponseEntity<UUID> unblockUser(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.unblockUser(id));
    }
}