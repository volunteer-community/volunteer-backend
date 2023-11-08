package com.maple.volunteer.dto.poster;


import com.maple.volunteer.dto.common.PaginationDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PosterListResponseDto {
    private CommunityHostResponseDto communityHostInfo;
    private List<PosterResponseDto> posterList;
    private PaginationDto paginationDto;

    @Builder
    public PosterListResponseDto(List<PosterResponseDto> posterList, PaginationDto paginationDto ,CommunityHostResponseDto communityHostInfo) {
        this.communityHostInfo = communityHostInfo;
        this.posterList = posterList;
        this.paginationDto = paginationDto;
    }
}

