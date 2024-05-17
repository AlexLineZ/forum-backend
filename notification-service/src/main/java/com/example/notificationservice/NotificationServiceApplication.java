package com.example.notificationservice;

import com.example.security.config.SecurityConfig;
import com.example.security.jwt.JwtRequestFilter;
import com.example.security.jwt.JwtTokenUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@EnableFeignClients(basePackages = { "com.example.security.client"})
@Import({SecurityConfig.class, JwtRequestFilter.class, JwtTokenUtils.class})
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

}
