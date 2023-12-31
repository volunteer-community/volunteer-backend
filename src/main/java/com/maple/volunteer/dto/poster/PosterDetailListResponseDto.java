package com.maple.volunteer.dto.poster;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PosterDetailListResponseDto {

    private PosterDetailResponseDto posterDetail;
    private CommentUserResponseDto commentUserResponseDto;

    @Builder
    public PosterDetailListResponseDto(PosterDetailResponseDto posterDetail ,CommentUserResponseDto commentUserResponseDto){
        this.posterDetail = posterDetail;
        this.commentUserResponseDto = commentUserResponseDto;
    }
}
