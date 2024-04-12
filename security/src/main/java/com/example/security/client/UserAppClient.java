package com.example.security.client;

import com.example.common.dto.UserDto;
import com.example.security.config.SecurityFeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "USER-APP", url = "http://localhost:8084", configuration = SecurityFeignClientConfiguration.class)
public interface UserAppClient {
    @GetMapping("api/users/person")
    UserDto getUserById(@RequestParam("userId") UUID userId);
}
