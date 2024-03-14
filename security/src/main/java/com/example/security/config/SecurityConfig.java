package com.example.security.config;

import com.example.security.jwt.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/api/users/update").authenticated()
                        .requestMatchers("/api/users/profile").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/forum/categories/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/forum/categories/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/forum/categories/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/forum/topics/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/forum/topics/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/forum/topics/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/forum/messages/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/forum/messages/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/forum/messages/**").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
