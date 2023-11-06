package com.maple.volunteer.security.jwt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@ToString
public class GeneratedToken {

    private String accessToken;
    private String refreshToken;
    private LocalDateTime accessTokenExpireTime;
    private LocalDateTime refreshTokenExpireTime;

    @Builder
    public GeneratedToken(String accessToken, String refreshToken, LocalDateTime accessTokenExpireTime, LocalDateTime refreshTokenExpireTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }
}
