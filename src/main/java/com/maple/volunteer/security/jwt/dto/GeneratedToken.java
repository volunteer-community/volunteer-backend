package com.maple.volunteer.security.jwt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class GeneratedToken {

    private String accessToken;
    private String refreshToken;
    private LocalDateTime accessTokenExpireTime;
}
