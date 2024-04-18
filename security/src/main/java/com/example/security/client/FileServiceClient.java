package com.example.security.client;

import com.example.common.dto.FileDto;
import com.example.common.dto.UserDto;
import com.example.security.config.SecurityFeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "FILE-SERVICE", url = "http://localhost:8083", configuration = SecurityFeignClientConfiguration.class)
public interface FileServiceClient {

    @GetMapping("/api/files/info")
    FileDto getFileInfo(@RequestParam("id") UUID id);

    @DeleteMapping("/api/files/delete/{id}")
    void deleteFile(@PathVariable UUID id, UserDto user);
}
