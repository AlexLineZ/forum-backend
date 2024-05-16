package com.example.userapp.service;

import com.example.common.dto.user.UserDto;
import com.example.userapp.dto.TokenResponse;

public interface TokenService {
    TokenResponse getTokens(UserDto user);
    TokenResponse refreshAccessToken(String refreshToken);
}
