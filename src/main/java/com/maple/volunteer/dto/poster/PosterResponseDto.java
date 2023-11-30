package com.maple.volunteer.dto.poster;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PosterResponseDto {
    private Long userId;    // 유저 ID
    private Long posterId; // 게시글 ID
    private String posterTitle;   // 게시글 제목
    private String posterContent; // 게시글 내용
    private String posterAuthor;  // 게시글 작성자
    private Integer heartCount;  // 게시글 좋아요 수
    private Integer commentCount; // 게시글 댓글 수
    private String posterImgPath; // 게시글 이미지
    private String profileImg; // 프로필 이미지
    private LocalDateTime posterCreatedAt;
    private LocalDateTime posterUpdatedAt;



    @Builder
    public PosterResponseDto(Long userId, Long posterId, String posterTitle,  String posterContent, String posterAuthor,
                              Integer heartCount,Integer commentCount,String posterImgPath,String profileImg,LocalDateTime posterCreatedAt, LocalDateTime posterUpdatedAt){
        this.userId = userId;
        this.posterId = posterId;
        this.posterTitle = posterTitle;
        this.posterContent = posterContent;
        this.posterAuthor = posterAuthor;
        this.heartCount = heartCount;
        this.commentCount = commentCount;
        this.posterImgPath = posterImgPath;
        this.profileImg = profileImg;
        this.posterCreatedAt = posterCreatedAt;
        this.posterUpdatedAt = posterUpdatedAt;
    }

}
