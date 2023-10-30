package com.maple.volunteer.security.jwt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseStatusDto {
    private Integer status;
    private Object data;

    public ResponseStatusDto(Integer status) {
        this.status = status;
    }

    public static ResponseStatusDto addStatus(Integer status) {
        return new ResponseStatusDto(status);
    }

    public static ResponseStatusDto success(){
        return new ResponseStatusDto(200);
    }

    public static ResponseStatusDto success(Object data) {
        return new ResponseStatusDto(200, data);
    }
}
