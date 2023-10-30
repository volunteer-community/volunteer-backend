package com.maple.volunteer.dto.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CommentListResponseDto {
    private List<CommentResponseDto> commentList;

    @Builder
    public CommentListResponseDto(List<CommentResponseDto> commentList) {
        this.commentList = commentList;
    }
}
