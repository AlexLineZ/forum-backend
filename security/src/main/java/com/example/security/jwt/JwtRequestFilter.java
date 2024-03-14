package com.example.security.jwt;

import com.example.common.dto.UserDto;
import com.example.security.client.UserAppClient;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserAppClient userAppClient;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                UUID userId = jwtTokenUtils.getUserIdFromToken(jwt);
                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    ResponseEntity<UserDto> responseEntity = userAppClient.getUserById(userId);
                    UserDto user = responseEntity.getBody();
                    if (user != null) {
                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                                user, jwt, Collections.emptyList()
                        );
                        SecurityContextHolder.getContext().setAuthentication(token);
                    }
                }
            } catch (ExpiredJwtException e) {
                log.debug("Token is expired :(");
            } catch (Exception e) {
                log.error("Error processing JWT", e);
            }
        }
        filterChain.doFilter(request, response);
    }
}
