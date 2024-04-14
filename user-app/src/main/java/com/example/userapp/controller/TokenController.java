package com.example.userapp.controller;

import com.example.userapp.dto.TokenResponse;
import com.example.userapp.dto.request.TokenRefreshRequest;
import com.example.userapp.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Refresh token")
@RequestMapping("api/token")
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/refresh-token")
    @Operation(
            summary = "Обновление токена",
            description = "Позволяет обновить accessToken при помощи refreshToken"
    )
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(tokenService.refreshAccessToken(request.refreshToken()));
    }
}
