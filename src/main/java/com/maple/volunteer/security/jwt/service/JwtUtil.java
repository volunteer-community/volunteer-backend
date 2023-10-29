package com.maple.volunteer.security.jwt.service;

import com.maple.volunteer.repository.user.UserRepository;
import com.maple.volunteer.security.jwt.config.JwtProperties;
import com.maple.volunteer.security.jwt.dto.GeneratedToken;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtUtil { // AccessToken, RefreshToken 발급 및 검증

    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(jwtProperties.getSecret().getBytes());
    }

    public GeneratedToken generateToken(String email, String role) {
        // accessToken, refreshToken 생성
        String accessToken = generateAccessToken(email, role);
        String refreshToken = generateRefreshToken(email, role);
        LocalDateTime accessTokenExpireTime = LocalDateTime.now().plus(30, ChronoUnit.MINUTES);

        return new GeneratedToken(accessToken, refreshToken, accessTokenExpireTime);
    }

    public String generateRefreshToken(String email, String role) {

        // 토큰 유효기간 설정
        long refreshPeriod = 1000L * 60L * 60L * 24L* 14L;

        // 새로운 클레임 객체 생성 후 이메일, 권한 설정
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);

        // 현재 시간 get
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims) // Payload를 구성하는 속성 정의
                .setIssuedAt(now) // 발행일자
                .setExpiration(new Date(now.getTime() + refreshPeriod)) // 만료일시
                .signWith(SignatureAlgorithm.HS256, secretKey) // 토큰 서명
                .compact();
    }

    public String generateAccessToken(String email, String role) {
        long tokenPeriod = 1000L * 60L * 30L;
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey) // secretKey 설정 후 파싱
                    .build()
                    .parseClaimsJws(token); // 주어준 토큰을 파싱하여 Claims 객체 get

            // 시간 비교
            return claims.getBody()
                    .getExpiration()
                    .after(new Date()); // 시간 비교를 통해 유효성 검사 결과 반환
        } catch (Exception e) {
            return false;
        }
    }

    // 추가 메소드 작성
    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getUserRole(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("role", String.class);
    }
}
