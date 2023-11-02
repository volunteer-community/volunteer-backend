package com.maple.volunteer.dto.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    //private Long posterId;
    private Long commentId;
    private String commentContent;
    private String commentAuthor;
    private String posterImg;


    @Builder
    public CommentResponseDto(Long commentId, String commentContent, String commentAuthor, String posterImg) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.commentAuthor = commentAuthor;
        this.posterImg = posterImg;
    }
}
