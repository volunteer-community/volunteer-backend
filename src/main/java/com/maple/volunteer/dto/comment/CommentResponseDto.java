package com.maple.volunteer.dto.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String commnentContent;
    private String commentAuthor;


    @Builder
    public CommentResponseDto(Long commentId, String commnentContent,String commentAuthor){
        this.commentId = commentId;
        this.commnentContent = commnentContent;
        this.commentAuthor = commentAuthor;
    }
}
