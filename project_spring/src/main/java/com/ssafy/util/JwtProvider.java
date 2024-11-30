package com.ssafy.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    private final String secretKeyString = "mySuperSecretKeyForJWTTokenShouldBeAtLeast32BytesLong";
    private final Key secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    
//    // test
//    private final long accessTokenValidity = 1000L * 60 * 1; // 1분
//    private final long refreshTokenValidity = 1000L * 60 * 2; // 2분
    
    private final long accessTokenValidity = 1000L * 60 * 15; // 15분
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; // 7일

    // 액세스 토큰 생성
    public String createAccessToken(String userId) {
        return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(String userId) {
        return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    // 토큰에서 userId 추출
    public String getUserIdFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
