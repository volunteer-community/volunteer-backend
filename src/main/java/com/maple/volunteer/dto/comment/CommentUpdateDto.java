package com.maple.volunteer.dto.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentUpdateDto {
    private String commentContent;

    @Builder
    public CommentUpdateDto(String commentContent){
        this.commentContent = commentContent;

    }
}
