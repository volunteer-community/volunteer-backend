package com.maple.volunteer.dto.community;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityImgPathDto {

    private String communityImgPath;

    @Builder
    public CommunityImgPathDto(String communityImgPath) {
        this.communityImgPath = communityImgPath;
    }
}
