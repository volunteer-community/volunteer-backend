package com.maple.volunteer.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;
    private LocalDateTime accessTokenExpireTime;
    private LocalDateTime refreshTokenExpireTime;

    @Builder
    public TokenDto(String accessToken, String refreshToken, LocalDateTime accessTokenExpireTime, LocalDateTime refreshTokenExpireTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }
}
