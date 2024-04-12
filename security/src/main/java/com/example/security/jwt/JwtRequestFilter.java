package com.example.security.jwt;

import com.example.common.client.UserAppClient;
import com.example.common.dto.UserDto;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserAppClient userAppClient;

    private final List<String> allowedPaths = List.of("/api/auth", "/api/token");

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        String requestPath = request.getRequestURI();

        if (allowedPaths.contains(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                UUID userId = jwtTokenUtils.getUserIdFromToken(jwt);
                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDto user = userAppClient.getUserById(userId);

                    if (user != null && user.isEnabled() && !user.isBlocked()) {
                        List<GrantedAuthority> authorities = Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_" + user.role().name())
                        );
                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                                user, jwt, authorities
                        );
                        SecurityContextHolder.getContext().setAuthentication(token);
                    } else if (user != null && user.isBlocked()) {
                        denyAccess(response, "Account is blocked.");
                        return;
                    } else {
                        denyAccess(response, "Account is not activated.");
                        return;
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

    private void denyAccess(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String errorMessage = "{\"message\": \"" + message + "\"}";
        response.getWriter().write(errorMessage);
    }
}


