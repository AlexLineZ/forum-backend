package com.example.userapp.service.user;

import com.example.common.dto.UserDto;
import com.example.common.exception.UserNotFoundException;
import com.example.security.jwt.JwtTokenUtils;
import com.example.userapp.dto.TokenResponse;
import com.example.userapp.entity.RefreshToken;
import com.example.userapp.entity.User;
import com.example.userapp.exception.InvalidTokenException;
import com.example.userapp.mapper.UserMapper;
import com.example.userapp.repository.RefreshTokenRepository;
import com.example.userapp.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RefreshTokenRepository refreshRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @Value("${jwt.refresh.expiration}")
    private Duration lifetime;

    public TokenResponse getTokens(UserDto user){
        String accessToken = jwtTokenUtils.generateAccessToken(user);
        String refreshToken = jwtTokenUtils.generateRefreshToken(user);

        RefreshToken refresh = getRefreshTokenEntity(refreshToken);
        refreshRepository.save(refresh);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refreshAccessToken(String refreshToken) {
        if (!isRefreshTokenValid(refreshToken)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        UUID userId = jwtTokenUtils.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException("Invalid login details"));

        return getTokens(UserMapper.userToUserDto(user));
    }

    private void deleteRefreshToken(UUID tokenId){
        refreshRepository.deleteById(tokenId);
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
