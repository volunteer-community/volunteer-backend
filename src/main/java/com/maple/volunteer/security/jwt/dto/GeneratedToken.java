package com.maple.volunteer.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class GeneratedToken {

    private String accessToken;
    private String refreshToken;
}
