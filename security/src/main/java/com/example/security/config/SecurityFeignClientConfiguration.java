package com.example.security.config;

import com.example.security.handler.SecurityErrorDecoder;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class SecurityFeignClientConfiguration {
    private final boolean authenticationRequired = true;

    @Bean
    public ErrorDecoder errorDecoder() {
        return new SecurityErrorDecoder();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authenticationRequired && authentication != null && authentication.isAuthenticated()) {
                template.header("Authorization", "Bearer " + authentication.getCredentials());
            }
        };
    }
}
