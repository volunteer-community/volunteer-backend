package com.maple.volunteer.dto.poster;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PosterResponseDto {
    private Long posterId; // 게시글 ID
    private String posterTitle;   // 게시글 제목
    private String posterContent; // 게시글 내용
    private String posterAuthor;  // 게시글 작성자
    private Integer heartCount;  // 게시글 좋아요 수
    private String posterImgPath; // 게시글 이미지
    private String profileImg;

    // 댓글 개수

    @Builder
    public PosterResponseDto(Long posterId, String posterTitle, String posterAuthor, String posterContent,
                              Integer heartCount,String posterImgPath,String profileImg){
        this.posterId = posterId;
        this.posterTitle = posterTitle;
        this.posterAuthor = posterAuthor;
        this.posterContent = posterContent;
        this.heartCount = heartCount;
        this.posterImgPath = posterImgPath;
        this.profileImg = profileImg;

    }

}
