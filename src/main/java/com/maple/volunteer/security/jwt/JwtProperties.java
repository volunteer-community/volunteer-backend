package com.maple.volunteer.security.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtProperties {

    @Value("${jwt.secret}")
    private String secret;


    // 나의 살던 고향은 꽃 피 는 산골골
}