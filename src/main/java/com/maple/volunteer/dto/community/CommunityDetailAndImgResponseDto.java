package com.maple.volunteer.dto.community;

import com.maple.volunteer.domain.community.Community;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CommunityDetailAndImgResponseDto {

    private CommunityUserDetailDto communityUserDetail;
    private CommunityDetailResponseDto communityDetail;
    private List<CommunityImgResponseDto> communityImgPathList;

    @Builder
    public CommunityDetailAndImgResponseDto(CommunityUserDetailDto communityUserDetail, CommunityDetailResponseDto communityDetail, List<CommunityImgResponseDto> communityImgPathList) {
        this.communityUserDetail = communityUserDetail;
        this.communityDetail = communityDetail;
        this.communityImgPathList = communityImgPathList;
    }
}
