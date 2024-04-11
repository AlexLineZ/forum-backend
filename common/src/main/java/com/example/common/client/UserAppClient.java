package com.example.common.client;

import com.example.common.config.CommonFeignClientConfiguration;
import com.example.common.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "USER-APP", url = "http://localhost:8084", configuration = CommonFeignClientConfiguration.class)
public interface UserAppClient {
    @GetMapping("api/users/person")
    UserDto getUserById(@RequestParam("userId") UUID userId);
}
