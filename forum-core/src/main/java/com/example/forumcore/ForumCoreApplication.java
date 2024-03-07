package com.example.forumcore;

import com.example.userapp.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@Import(SecurityConfig.class)
@ComponentScan(basePackages = {"com.example.forumcore", "com.example.userapp"})
public class ForumCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumCoreApplication.class, args);
    }

}
