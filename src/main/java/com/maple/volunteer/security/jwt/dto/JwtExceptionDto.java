package com.maple.volunteer.security.jwt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtExceptionDto {

    private String status;
    private String message;
    private String data;

    @Builder
    public JwtExceptionDto(String status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }
}
