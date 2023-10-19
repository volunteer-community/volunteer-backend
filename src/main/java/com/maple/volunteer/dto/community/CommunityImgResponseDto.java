package com.maple.volunteer.dto.community;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityImgResponseDto {

    private Integer communityImgNum;
    private String communityImgPath;

    @Builder
    public CommunityImgResponseDto(Integer communityImgNum, String communityImgPath) {
        this.communityImgNum = communityImgNum;
        this.communityImgPath = communityImgPath;
    }
}
