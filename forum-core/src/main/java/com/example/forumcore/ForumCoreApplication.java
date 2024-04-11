package com.example.forumcore;

import com.example.security.config.SecurityConfig;
import com.example.security.jwt.JwtRequestFilter;
import com.example.security.jwt.JwtTokenUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = { "com.example.common.client", "com.example.forumcore.client"})
@Import({SecurityConfig.class, JwtRequestFilter.class, JwtTokenUtils.class})
public class ForumCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumCoreApplication.class, args);
    }

}
