package com.example.security.client;

import com.example.common.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "USER-SERVICE")
public interface UserAppClient {
    @GetMapping("api/users/person")
    ResponseEntity<UserDto> getUserById(@RequestParam("userId") UUID userId);
}
