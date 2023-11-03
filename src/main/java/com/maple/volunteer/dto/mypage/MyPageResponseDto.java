package com.maple.volunteer.dto.mypage;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyPageResponseDto {
    private int communityUserCount; // 내가 가입한 커뮤니티 개수
    private int countOfPosterLike; // 좋아요한 게시글 개수
    private int countOfLikedPoster; // 좋아요 받은 게시글 개수
    private int commentCount; // 내가 쓴 댓글 개수

    @Builder
    public MyPageResponseDto(int communityUserCount,int countOfPosterLike ,int countOfLikedPoster, int commentCount){
        this.communityUserCount = communityUserCount;
        this.countOfPosterLike = countOfPosterLike;
        this.countOfLikedPoster = countOfLikedPoster;
        this.commentCount = commentCount;
    }
}
