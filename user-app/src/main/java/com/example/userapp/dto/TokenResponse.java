package com.example.userapp.dto;

import lombok.Builder;

@Builder
public record TokenResponse(String accessToken) { }
