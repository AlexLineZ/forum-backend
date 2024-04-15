package com.example.userapp;

import com.example.security.config.SecurityConfig;
import com.example.security.jwt.JwtRequestFilter;
import com.example.security.jwt.JwtTokenUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@EnableFeignClients(basePackages = "com.example.security.client")
@EnableScheduling
@Import({SecurityConfig.class, JwtRequestFilter.class, JwtTokenUtils.class})
public class UserAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserAppApplication.class, args);
    }

}
