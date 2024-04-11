package com.example.forumcore.config;

import com.example.forumcore.handler.ForumErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ForumFeignClientConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new ForumErrorDecoder();
    }
}
