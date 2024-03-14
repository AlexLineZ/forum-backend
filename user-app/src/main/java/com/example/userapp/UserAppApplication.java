package com.example.userapp;

import com.example.security.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@Import(SecurityConfig.class)
@ComponentScan(basePackages = {"com.example.userapp", "com.example.security"})
public class UserAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserAppApplication.class, args);
    }

}
