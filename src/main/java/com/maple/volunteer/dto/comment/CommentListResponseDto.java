package com.maple.volunteer.dto.comment;

import com.maple.volunteer.dto.common.PaginationDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CommentListResponseDto {
    private List<CommentResponseDto> commentList;
    private PaginationDto paginationDto;

    @Builder
    public CommentListResponseDto(List<CommentResponseDto> commentList, PaginationDto paginationDto) {

        this.commentList = commentList;
        this.paginationDto = paginationDto;
    }
}
