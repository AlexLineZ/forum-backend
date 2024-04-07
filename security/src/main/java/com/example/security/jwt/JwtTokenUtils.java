package com.example.security.jwt;

import com.example.common.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtAccessLifetime;

    @Value("${jwt.refresh.expiration}")
    private Duration jwtRefreshLifetime;

    public String generateAccessToken(UserDto user){
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtAccessLifetime.toMillis());
        UUID tokenId = UUID.randomUUID();

        return Jwts.builder()
                .setSubject(user.email())
                .claim("userId", user.id().toString())
                .claim("role", user.role())
                .setId(tokenId.toString())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDto user){
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtRefreshLifetime.toMillis());
        UUID tokenId = UUID.randomUUID();

        return Jwts.builder()
                .setSubject(user.email())
                .claim("userId", user.id().toString())
                .claim("role", user.role())
                .setId(tokenId.toString())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public UUID getUserIdFromToken(String token){
        String userId = getAllClaimsFromToken(token).get("userId", String.class);
        return UUID.fromString(userId);
    }

    public long getRemainingTimeInMillis(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        long currentTimeInMillis = System.currentTimeMillis();
        return expirationDate.getTime() - currentTimeInMillis;
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    public String getIdFromToken(String token) {
        return getAllClaimsFromToken(token).getId();
    }

    public SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

