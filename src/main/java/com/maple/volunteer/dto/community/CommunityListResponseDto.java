package com.maple.volunteer.dto.community;

import com.maple.volunteer.dto.common.PaginationDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CommunityListResponseDto {

    private List<CommunityResponseDto> communityList;
    private PaginationDto paginationDto;

    @Builder
    public CommunityListResponseDto(List<CommunityResponseDto> communityList, PaginationDto paginationDto) {
        this.communityList = communityList;
        this.paginationDto = paginationDto;
    }
}
