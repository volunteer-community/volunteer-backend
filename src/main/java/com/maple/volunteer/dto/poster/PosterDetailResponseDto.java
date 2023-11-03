package com.maple.volunteer.dto.poster;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PosterDetailResponseDto {

    private Long posterId;
    private String posterTitle;
    private String posterAuthor;
    private String posterContent;
    private int heartCount;
    private String posterImgPath;
    private String profileImg;
    private LocalDateTime posterCreatedAt;
    private LocalDateTime posterUpdatedAt;

    @Builder
    public PosterDetailResponseDto(Long posterId, String posterTitle, String posterAuthor, String posterContent, int heartCount, String posterImgPath, String profileImg, LocalDateTime posterCreatedAt, LocalDateTime posterUpdatedAt) {
        this.posterId = posterId;
        this.posterTitle = posterTitle;
        this.posterAuthor = posterAuthor;
        this.posterContent = posterContent;
        this.heartCount = heartCount;
        this.posterImgPath = posterImgPath;
        this.profileImg = profileImg;
        this.posterCreatedAt = posterCreatedAt;
        this.posterUpdatedAt = posterUpdatedAt;
    }
}
