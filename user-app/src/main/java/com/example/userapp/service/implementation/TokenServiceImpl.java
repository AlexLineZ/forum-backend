package com.example.userapp.service.implementation;

import com.example.common.dto.user.UserDto;
import com.example.common.exception.UserNotFoundException;
import com.example.security.jwt.JwtTokenUtils;
import com.example.userapp.dto.TokenResponse;
import com.example.userapp.entity.RefreshToken;
import com.example.userapp.entity.User;
import com.example.userapp.exception.InvalidTokenException;
import com.example.userapp.mapper.UserMapper;
import com.example.userapp.repository.RefreshTokenRepository;
import com.example.userapp.repository.UserRepository;
import com.example.userapp.service.TokenService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final RefreshTokenRepository refreshRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @Value("${jwt.refresh.expiration}")
    private Duration lifetime;

    @Scheduled(fixedRate = 86400000)
    @Transactional
    public void cleanupExpiredTokens() {
        Instant now = Instant.now();
        refreshRepository.deleteAllByExpirationDateTimeBefore(now);
    }

    @Override
    public TokenResponse getTokens(UserDto user){
        String accessToken = jwtTokenUtils.generateAccessToken(user);
        String refreshToken = jwtTokenUtils.generateRefreshToken(user);

        RefreshToken refresh = getRefreshTokenEntity(refreshToken);
        refreshRepository.save(refresh);

        return new TokenResponse(accessToken, refreshToken);
    }

    @Override
    public TokenResponse refreshAccessToken(String refreshToken) {
        if (!isRefreshTokenValid(refreshToken)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        UUID userId = jwtTokenUtils.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException("Invalid login details"));

        return getTokens(UserMapper.mapUserToUserDto(user));
    }

    private boolean isRefreshTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtTokenUtils.getSignKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private RefreshToken getRefreshTokenEntity(String token){
        UUID userId = jwtTokenUtils.getUserIdFromToken(token);
        User user = userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException("Invalid login details"));

        return RefreshToken.builder()
                .user(user)
                .token(token)
                .expirationDateTime(Instant.now().plus(lifetime))
                .build();
    }
}
