package com.maple.volunteer.dto.community;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CommunityDetailAndImgResponseDto {

    private CommunityDetailResponseDto communityDetail;
    private List<CommunityImgResponseDto> communityImgPathList;

    @Builder
    public CommunityDetailAndImgResponseDto(CommunityDetailResponseDto communityDetail, List<CommunityImgResponseDto> communityImgPathList) {
        this.communityDetail = communityDetail;
        this.communityImgPathList = communityImgPathList;
    }
}
