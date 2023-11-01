package com.maple.volunteer.dto.poster;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PosterImgResponseDto {

    private Integer posterImgNum;
    private String posterImgPath;

    @Builder
    public PosterImgResponseDto(Integer posterImgNum, String posterImgPath){
        this.posterImgNum = posterImgNum;
        this.posterImgPath = posterImgPath;
    }
}
