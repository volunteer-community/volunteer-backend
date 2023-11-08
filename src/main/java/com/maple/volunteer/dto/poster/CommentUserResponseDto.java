package com.maple.volunteer.dto.poster;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentUserResponseDto {
    private Long commentUserId;
    private String commentUserNickName;
    private String commentUserProfileImg;

    @Builder
    public CommentUserResponseDto(Long commentUserId, String commentUserNickName,String commentUserProfileImg ) {
        this.commentUserId = commentUserId;
        this.commentUserNickName = commentUserNickName;
        this.commentUserProfileImg = commentUserProfileImg;
    }
}
