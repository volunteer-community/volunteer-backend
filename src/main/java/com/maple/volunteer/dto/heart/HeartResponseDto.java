package com.maple.volunteer.dto.heart;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HeartResponseDto {
    private Boolean heartStatus;

    @Builder
    public HeartResponseDto(Boolean heartStatus){
        this.heartStatus = heartStatus;
    }
}
