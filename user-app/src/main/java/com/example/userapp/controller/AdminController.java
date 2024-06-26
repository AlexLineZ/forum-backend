package com.example.userapp.controller;

import com.example.common.dto.page.PageResponse;
import com.example.common.dto.user.UserDto;
import com.example.common.enums.Role;
import com.example.userapp.dto.request.admin.AdminRegisterRequest;
import com.example.userapp.dto.request.admin.AdminUpdateRequest;
import com.example.userapp.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    @Operation(
            summary = "Создание пользователя администратором",
            description = "Позволяет администратору создать пользователя"
    )
    public ResponseEntity<UUID> createUser(
            @Valid @RequestBody AdminRegisterRequest user
    ) {
        return ResponseEntity.ok(adminService.createUser(user));
    }

    @PutMapping("/user/{id}")
    @Operation(
            summary = "Изменение пользователя администратором",
            description = "Позволяет администратору изменить данные пользователя"
    )
    public ResponseEntity<UUID> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody AdminUpdateRequest user
    ) {
        return ResponseEntity.ok(adminService.updateUser(id, user));
    }

    @PatchMapping("/user/{id}/role")
    @Operation(
            summary = "Изменение роли пользователя",
            description = "Позволяет администратору изменить роль пользователя"
    )
    public ResponseEntity<UUID> updateUserRole(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "USER") Role role,
            @AuthenticationPrincipal UserDto admin
    ) {
        return ResponseEntity.ok(adminService.updateUserRole(id, role, admin));
    }

    //не делал удаление пользователя, иначе пришлось бы удалять все созданные сообщения и тд, поэтому оставил только бан
    @PatchMapping("/user/{id}/block")
    @Operation(
            summary = "Блокировка пользователя",
            description = "Позволяет администратору заблокировать пользователя"
    )
    public ResponseEntity<UUID> blockUser(@PathVariable UUID id, @AuthenticationPrincipal UserDto admin) {
        return ResponseEntity.ok(adminService.blockUser(id, admin));
    }

    @PatchMapping("/user/{id}/unblock")
    @Operation(
            summary = "Разблокировка пользователя",
            description = "Позволяет администратору разблокировать пользователя"
    )
    public ResponseEntity<UUID> unblockUser(@PathVariable UUID id, @AuthenticationPrincipal UserDto admin) {
        return ResponseEntity.ok(adminService.unblockUser(id, admin));
    }

    @GetMapping("/users")
    @Operation(summary = "Получение списка всех пользователей",
            description = "Возвращает страницу с пользователями")
    public ResponseEntity<PageResponse<UserDto>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(adminService.findAllUsers(pageable));
    }
}
