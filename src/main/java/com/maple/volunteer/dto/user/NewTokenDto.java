package com.maple.volunteer.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewTokenDto {

    private String accessToken;
    private LocalDateTime accessTokenExpireTime;
}
