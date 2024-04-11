package com.example.forumcore.client;

import com.example.common.dto.FileDto;
import com.example.forumcore.config.ForumFeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@FeignClient(name = "FILE-SERVICE", url = "http://localhost:8083", configuration = ForumFeignClientConfiguration.class)
public interface FileServiceClient {

    @GetMapping("/api/files/info")
    FileDto getFileInfo(@RequestParam("id") UUID id);
}