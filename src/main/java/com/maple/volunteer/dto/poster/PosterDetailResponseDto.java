package com.maple.volunteer.dto.poster;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PosterDetailResponseDto {

    private Long posterId;
    private String posterTitle;
    private String posterAuthor;
    private String posterContent;
    private int heartCount;
    private String posterImgPath;

    @Builder
    public PosterDetailResponseDto(Long posterId, String posterTitle, String posterAuthor, String posterContent,int heartCount,String posterImgPath){
        this.posterId = posterId;
        this.posterTitle = posterTitle;
        this.posterAuthor = posterAuthor;
        this.posterContent = posterContent;
        this.heartCount = heartCount;
        this.posterImgPath = posterImgPath;
    }

}
