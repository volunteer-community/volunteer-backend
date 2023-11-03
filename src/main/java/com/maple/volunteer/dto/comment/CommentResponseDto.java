package com.maple.volunteer.dto.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    //private Long posterId;
    private Long commentId;
    private String commentContent;
    private String commentAuthor;
    private LocalDateTime commentCreatedAt;
    private LocalDateTime commentUpdatedAt;
    private String profileImg;



    @Builder
    public CommentResponseDto(Long commentId, String commentContent, String commentAuthor, LocalDateTime commentCreatedAt, LocalDateTime commentUpdatedAt ,String profileImg ) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.commentAuthor = commentAuthor;
        this.commentCreatedAt = commentCreatedAt;
        this.commentUpdatedAt = commentUpdatedAt;
        this.profileImg = profileImg;

    }
}
