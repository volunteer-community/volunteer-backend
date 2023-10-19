package com.maple.volunteer.dto.community;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityResponseDto {

    private String communityId;
    private String communityTitle;
    private Integer communityParticipant;
    private String communityAuthor;
    private String communityStatus;
    private String communityIntroduce;
    private String communityMainImgPath;

    @Builder
    public CommunityResponseDto(String communityId, String communityTitle, Integer communityParticipant, String communityAuthor, String communityStatus, String communityIntroduce, String communityMainImgPath) {
        this.communityId = communityId;
        this.communityTitle = communityTitle;
        this.communityParticipant = communityParticipant;
        this.communityAuthor = communityAuthor;
        this.communityStatus = communityStatus;
        this.communityIntroduce = communityIntroduce;
        this.communityMainImgPath = communityMainImgPath;
    }
}
