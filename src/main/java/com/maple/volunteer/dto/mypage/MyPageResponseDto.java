package com.maple.volunteer.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {
    private String picture;
    private String nickname;
    private String email;
    private int communityUserCount; // 내가 가입한 커뮤니티 개수
    private int countOfPosterLike; // 좋아요한 게시글 개수
    private int countOfLikedPoster; // 좋아요 받은 게시글 개수
    private int commentCount; // 내가 쓴 댓글 개수


//    public MyPageResponseDto(String picture, String nickname, String email,
//            int communityUserCount,int countOfPosterLike ,int countOfLikedPoster, int commentCount){
//        this.picture = picture;
//        this.nickname = nickname;
//        this.email = email;
//        this.communityUserCount = communityUserCount;
//        this.countOfPosterLike = countOfPosterLike;
//        this.countOfLikedPoster = countOfLikedPoster;
//        this.commentCount = commentCount;
//    }
}
