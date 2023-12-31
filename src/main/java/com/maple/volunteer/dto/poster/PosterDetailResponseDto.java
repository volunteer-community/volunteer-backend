package com.maple.volunteer.dto.poster;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PosterDetailResponseDto {
    private Long userId;
    private Long posterId;
    private String posterTitle;
    private String posterAuthor;
    private String posterContent;
    private int heartCount;
    private int commentCount;
    private String posterImgPath;
    private String profileImg;
    private LocalDateTime posterCreatedAt;
    private LocalDateTime posterUpdatedAt;

    @Builder
    public PosterDetailResponseDto(Long userId, Long posterId, String posterTitle, String posterAuthor, String posterContent, int heartCount, int commentCount,String posterImgPath, String profileImg, LocalDateTime posterCreatedAt, LocalDateTime posterUpdatedAt) {
        this.userId = userId;
        this.posterId = posterId;
        this.posterTitle = posterTitle;
        this.posterAuthor = posterAuthor;
        this.posterContent = posterContent;
        this.heartCount = heartCount;
        this.commentCount = commentCount;
        this.posterImgPath = posterImgPath;
        this.profileImg = profileImg;
        this.posterCreatedAt = posterCreatedAt;
        this.posterUpdatedAt = posterUpdatedAt;
    }
}
