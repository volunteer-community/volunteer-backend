package com.maple.volunteer.dto.community;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityUserDetailDto {

    private String communityAuthor;
    private String communityUserProfile;

    @Builder
    public CommunityUserDetailDto(String communityAuthor, String communityUserProfile) {
        this.communityAuthor = communityAuthor;
        this.communityUserProfile = communityUserProfile;
    }
}
