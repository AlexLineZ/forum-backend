package com.example.common.config;

import com.example.common.handler.CommonErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonFeignClientConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CommonErrorDecoder();
    }
}
